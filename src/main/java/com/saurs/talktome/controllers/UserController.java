package com.saurs.talktome.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.saurs.talktome.dtos.UserDTO;
import com.saurs.talktome.models.User;
import com.saurs.talktome.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
  
  @Autowired
  private UserService service;

  @GetMapping
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    List<UserDTO> users = service.findAll();
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

  @PostMapping
  public ResponseEntity<User> insertUser(@RequestBody User user) {
    User newUser = service.addUser(user);
    URI uri = ServletUriComponentsBuilder
    .fromCurrentRequest()
    .path("/{id}")
    .buildAndExpand(newUser.getId())
    .toUri();

    return ResponseEntity.created(uri).body(newUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) {
    User updatedUser = service.updateUser(user, id);
    return ResponseEntity.ok().body(updatedUser);
  }

  @PatchMapping("/{userId}/partner/{partnerId}")
  public ResponseEntity<List<User>> setUserPartner(@PathVariable Long userId, @PathVariable Long partnerId) {
    List<User> userAndPartner = service.setPartner(userId, partnerId);
    return ResponseEntity.ok().body(userAndPartner);
  }

  @PatchMapping("/removePartner/{id}")
  public ResponseEntity<User> removeUserPartner(@PathVariable Long id) {
    User user = service.removePartner(id);
    return ResponseEntity.ok().body(user);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    service.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
