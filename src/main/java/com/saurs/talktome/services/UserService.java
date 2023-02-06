package com.saurs.talktome.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.saurs.talktome.models.Post;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saurs.talktome.dtos.UserDTO;
import com.saurs.talktome.models.User;
import com.saurs.talktome.repositories.UserRepository;
import com.saurs.talktome.services.exceptions.DatabaseException;
import com.saurs.talktome.services.exceptions.ObjectNotFoundException;
import com.saurs.talktome.utils.ServiceUtils;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  private PasswordEncoder delegateEncoder =
          PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public List<UserDTO> findAll(Pageable pageable) {
    return repository.findAll(pageable)
            .stream()
            .map(UserDTO::converter)
            .collect(Collectors.toList());
  }

  public UserDTO findById(Long id) {
    Optional<UserDTO> getUser = repository.findById(id).map(UserDTO::converter);
    return getUser.orElseThrow(() -> new ObjectNotFoundException(id));
  }

  public User addUser(User obj) {
    obj.setPassword(delegateEncoder.encode(obj.getPassword()));
    return repository.save(obj);
  }

  public User updateUser(User obj, Long id) {
    // TODO get authenticated user and check if it's him
    Optional<User> oldUser = repository.findById(id);
    BeanUtils.copyProperties(obj, oldUser.orElseThrow(
            () -> new ObjectNotFoundException(id)),
            ServiceUtils.getNullPropertyNames(obj));
    return repository.save(oldUser.get());
  }

  public void deleteUser(Long id) {
    // TODO get authenticated user and check if it's him
    try {
      User user = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));
      repository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
        throw new DatabaseException(e.getMessage());
    }
  }
  
}
