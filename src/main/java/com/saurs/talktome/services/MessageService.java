package com.saurs.talktome.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.saurs.talktome.dtos.MessageDTO;
import com.saurs.talktome.models.Message;
import com.saurs.talktome.models.User;
import com.saurs.talktome.repositories.MessageRepository;
import com.saurs.talktome.repositories.UserRepository;
import com.saurs.talktome.services.exceptions.AlreadySetException;
import com.saurs.talktome.services.exceptions.DatabaseException;
import com.saurs.talktome.services.exceptions.ObjectNotFoundException;
import com.saurs.talktome.services.exceptions.UserIsNotPartnerException;
import com.saurs.talktome.utils.ServiceUtils;

@Service
public class MessageService {

  @Autowired
  private MessageRepository repository;

  @Autowired
  private UserRepository userRepository;

  public List<MessageDTO> findAll() {
    return repository.findAll().stream().map(MessageDTO::converter).collect(Collectors.toList());
  }

  public MessageDTO findById(Long id) {
    Optional<MessageDTO> getMessage = repository.findById(id).map(MessageDTO::converter);
    return getMessage.orElseThrow(() -> new ObjectNotFoundException(id));
  }

  public List<MessageDTO> findAllByFromUser(Long id) {
    return repository.findAllByFromUserId(id).stream().map(MessageDTO::converter).collect(Collectors.toList());
  }

  public List<MessageDTO> findAllByToUser(Long id) {
    return repository.findAllByToUserId(id).stream().map(MessageDTO::converter).collect(Collectors.toList());
  }

  public Message addMessage(Message obj, Long fromId) {
    User fromUser = userRepository.findById(fromId).orElseThrow(() -> new ObjectNotFoundException(fromId));
    Long toId = fromUser.getPartner().getId();
    User toUser = userRepository.findById(toId).orElseThrow(() -> new ObjectNotFoundException(toId));
    if (fromUser.getPartner() == null) {
      throw new AlreadySetException("User doesn't have a partner.");
    }
    if (!fromUser.getPartner().getId().equals(toUser.getId())) {
      throw new UserIsNotPartnerException();
    }
    obj.setFromUser(fromUser);
    obj.setToUser(toUser);
    fromUser.getMessages().add(obj);
    return repository.save(obj);
  }

  public Message updateMessage(Message obj, Long id) {
    Optional<Message> oldMessage = repository.findById(id);
    BeanUtils.copyProperties(obj, oldMessage.orElseThrow(() -> new ObjectNotFoundException(id)), ServiceUtils.getNullPropertyNames(obj));
    return repository.save(oldMessage.get());
  }

  public void deleteMessage(Long id) {
    try {
      repository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
        throw new DatabaseException(e.getMessage());
    }
  }
  
}
