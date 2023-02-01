package com.saurs.talktome.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.saurs.talktome.dtos.UserDTO;
import com.saurs.talktome.models.User;
import com.saurs.talktome.repositories.UserRepository;
import com.saurs.talktome.services.exceptions.AlreadySetException;
import com.saurs.talktome.services.exceptions.DatabaseException;
import com.saurs.talktome.services.exceptions.ObjectNotFoundException;
import com.saurs.talktome.utils.ServiceUtils;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  public List<UserDTO> findAll() {
    return repository.findAll().stream().map(UserDTO::converter).collect(Collectors.toList());
  }

  public UserDTO findById(Long id) {
    Optional<UserDTO> getUser = repository.findById(id).map(UserDTO::converter);
    return getUser.orElseThrow(() -> new ObjectNotFoundException(id));
  }

  public User addUser(User obj) {
    return repository.save(obj);
  }

  public User updateUser(User obj, Long id) {
    Optional<User> oldUser = repository.findById(id);
    BeanUtils.copyProperties(obj, oldUser.orElseThrow(() -> new ObjectNotFoundException(id)), ServiceUtils.getNullPropertyNames(obj));
    return repository.save(oldUser.get());
  }

  public void setPartner(Long userId, Long partnerId) {
    User user = repository.findById(userId).orElseThrow(() -> new ObjectNotFoundException(userId));
    User partner = repository.findById(partnerId).orElseThrow(() -> new ObjectNotFoundException(partnerId));
    if (user.getPartner() != null || partner.getPartner() != null) {
      throw new AlreadySetException("User already have a partner. Make sure you remove the partner before trying to set a new one.");
    }
    user.setPartner(partner);
    partner.setPartner(user);
    repository.saveAll(Arrays.asList(user, partner));
  }

  public void removePartner(Long userId) {
    User user = repository.findById(userId).orElseThrow(() -> new ObjectNotFoundException(userId));
    if (user.getPartner() == null) {
      throw new AlreadySetException("User doesn't have a partner to remove.");
    }
    user.getPartner().setPartner(null);
    user.setPartner(null);
    repository.save(user);
  }

  public void deleteUser(Long id) {
    try {
      User user = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));
      if (user.getPartner() != null) {
        removePartner(id);
      }
      repository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
        throw new DatabaseException(e.getMessage());
    }
  }
  
}
