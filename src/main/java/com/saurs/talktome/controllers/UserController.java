package com.saurs.talktome.controllers;

import com.saurs.talktome.dtos.UserDTO;
import com.saurs.talktome.models.User;
import com.saurs.talktome.repositories.UserRepository;
import com.saurs.talktome.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.saurs.talktome.utils.ControllerUtils.getAuthenticatedUser;

@RestController
@RequestMapping(value = "/users")
public class UserController {

  @Autowired
  private UserService service;

  @Autowired
  private UserRepository repository;

  @GetMapping
  public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "20") Integer size) {
    List<UserDTO> users = service.findAll(PageRequest.of(page, size));
    return ResponseEntity.ok().body(users);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    UserDTO user = service.findById(id);
     if (user == null) {
      return ResponseEntity.notFound().build();
     }
     return ResponseEntity.ok().body(user);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> updateUser(@RequestBody User user, @PathVariable Long id, @AuthenticationPrincipal UserDetails activeUser) {
    Long userId = repository.findByUsername(activeUser.getUsername()).get().getId();
    if (userId.equals(id)) {
      UserDTO updatedUser = UserDTO.converter(service.updateUser(user, id));
      return ResponseEntity.ok().body(updatedUser);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id, @AuthenticationPrincipal User activeUser) {
    Long userId = repository.findByUsername(activeUser.getUsername()).get().getId();
    if (userId.equals(id)) {
      service.deleteUser(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
