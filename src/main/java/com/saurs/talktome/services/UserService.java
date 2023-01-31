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
import com.saurs.talktome.services.exceptions.ResourceNotFoundException;
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
    return getUser.orElseThrow(() -> new ResourceNotFoundException(id));
  }

  public User addUser(User obj) {
    return repository.save(obj);
  }

  public User updateUser(User obj, Long id) {
    Optional<User> oldUser = repository.findById(id);
    BeanUtils.copyProperties(obj, oldUser.orElseThrow(() -> new ResourceNotFoundException(id)), ServiceUtils.getNullPropertyNames(obj));
    return repository.save(oldUser.get());
  }

  public List<User> setPartner(Long userId, Long partnerId) {
    User user = repository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(userId));
    User partner = repository.findById(partnerId).orElseThrow(() -> new ResourceNotFoundException(partnerId));
    if (user.getPartner() != null || partner.getPartner() != null) {
      throw new AlreadySetException();
    }
    user.setPartner(partner);
    partner.setPartner(user);
    return repository.saveAll(Arrays.asList(user, partner));
  }

  public User removePartner(Long userId) {
    User user = repository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(userId));
    user.getPartner().setPartner(null);
    user.setPartner(null);
    return repository.save(user);
  }

  public void deleteUser(Long id) {
    try {
      User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
      if (user.getPartner() != null) {
        removePartner(id);
      }
      repository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
        throw new DatabaseException(e.getMessage());
    }
  }
  
}
