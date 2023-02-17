package com.saurs.talktome.services;

import com.saurs.talktome.dtos.UserDTO;
import com.saurs.talktome.models.User;
import com.saurs.talktome.repositories.UserRepository;
import com.saurs.talktome.services.exceptions.DatabaseException;
import com.saurs.talktome.services.exceptions.ObjectNotFoundException;
import com.saurs.talktome.utils.ServiceUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

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

  public User updateUser(User obj, Long id) {
    Optional<User> oldUser = repository.findById(id);
    BeanUtils.copyProperties(obj, oldUser.orElseThrow(
            () -> new ObjectNotFoundException(id)),
            ServiceUtils.getNullPropertyNames(obj));
    return repository.save(oldUser.get());
  }

  public void deleteUser(Long id) {

    try {
      User user = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id));
      user.getRoles().clear();
      repository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
        throw new DatabaseException(e.getMessage());
    }
  }
  
}
