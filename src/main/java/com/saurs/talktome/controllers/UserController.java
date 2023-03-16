package com.saurs.talktome.controllers;

import com.saurs.talktome.dtos.UserDTO;
import com.saurs.talktome.models.User;
import com.saurs.talktome.repositories.UserRepository;
import com.saurs.talktome.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
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

  @PutMapping("/update")
  public ResponseEntity<UserDTO> updateUser(@RequestBody User user, @AuthenticationPrincipal UserDetails activeUser) {
      Long id = repository.findByUsername(activeUser.getUsername()).get().getId();
      UserDTO updatedUser = UserDTO.converter(service.updateUser(user, id));
      return ResponseEntity.ok().body(updatedUser);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User activeUser) {
      Long id = repository.findByUsername(activeUser.getUsername()).get().getId();
      service.deleteUser(id);
      return ResponseEntity.noContent().build();
  }
}
