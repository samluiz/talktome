package com.saurs.talktome.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.saurs.talktome.dtos.MessageDTO;
import com.saurs.talktome.models.Message;
import com.saurs.talktome.repositories.MessageRepository;
import com.saurs.talktome.services.MessageService;

@RestController
@RequestMapping(value = "/api/messages")
public class MessageController {
  
  @Autowired
  private MessageService service;

  @Autowired
  private MessageRepository repository;

  @GetMapping
  public ResponseEntity<List<MessageDTO>> getAllMessages() {
    List<MessageDTO> messages = service.findAll();
    return ResponseEntity.ok().body(messages);
  }

  @GetMapping("/{id}")
  public ResponseEntity<MessageDTO> getMessageById(@PathVariable Long id) {
    MessageDTO message = service.findById(id);
     if (message == null) {
      return ResponseEntity.notFound().build();
     }
     return ResponseEntity.ok().body(message);
  }

  @GetMapping("/sent/user/{userId}")
  public ResponseEntity<List<MessageDTO>> getMessagesFromUser(@PathVariable Long userId) {
    List<MessageDTO> messages = service.findAllByFromUser(userId);
     return ResponseEntity.ok().body(messages);
  }

  @GetMapping("/received/user/{userId}")
  public ResponseEntity<List<MessageDTO>> getMessagesReceivedFromUser(@PathVariable Long userId) {
    List<MessageDTO> messages = service.findAllByToUser(userId);
     return ResponseEntity.ok().body(messages);
  }

  @PostMapping("/user/{userId}/post")
  public ResponseEntity<MessageDTO> postMessage(@RequestBody Message message, @PathVariable Long userId) {
    Message newMessage = service.addMessage(message, userId);
    URI uri = ServletUriComponentsBuilder
    .fromCurrentRequest()
    .path("/{id}")
    .buildAndExpand(newMessage.getId())
    .toUri();

    MessageDTO msgDto = repository.findById(newMessage.getId()).map(MessageDTO::converter).get();

    return ResponseEntity.created(uri).body(msgDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<MessageDTO> updateMessage(@RequestBody Message message, @PathVariable Long id) {
    Message updatedMessage = service.updateMessage(message, id);

    MessageDTO msgDto = repository.findById(updatedMessage.getId()).map(MessageDTO::converter).get();
    return ResponseEntity.ok().body(msgDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
    service.deleteMessage(id);
    return ResponseEntity.noContent().build();
  }
}
