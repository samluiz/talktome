package com.saurs.talktome.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.saurs.talktome.dtos.UserDTO;
import com.saurs.talktome.models.User;
import com.saurs.talktome.services.UserService;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {
  
  @Autowired
  private UserService service;

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

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    service.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
