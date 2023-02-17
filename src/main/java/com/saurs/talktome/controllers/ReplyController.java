package com.saurs.talktome.controllers;

import com.saurs.talktome.dtos.ReplyDTO;
import com.saurs.talktome.models.Reply;
import com.saurs.talktome.repositories.ReplyRepository;
import com.saurs.talktome.repositories.UserRepository;
import com.saurs.talktome.services.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import static com.saurs.talktome.utils.ControllerUtils.getAuthenticatedUser;

@RestController
@RequestMapping(value = "/replies")
public class ReplyController {
  @Autowired
  private ReplyService service;

  @Autowired
  private ReplyRepository repository;

  @Autowired
  private UserRepository userRepository;

  @GetMapping
  public ResponseEntity<List<ReplyDTO>> getAllReplies(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                      @RequestParam(value = "size", defaultValue = "20") Integer size) {
    List<ReplyDTO> replies = service.findAll(PageRequest.of(page, size));
    return ResponseEntity.ok().body(replies);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReplyDTO> getReplyById(@PathVariable Long id) {
    ReplyDTO reply = service.findById(id);
     if (reply == null) {
      return ResponseEntity.notFound().build();
     }
     return ResponseEntity.ok().body(reply);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ReplyDTO>> getRepliesFromUser(
          @PathVariable Long userId,
          @RequestParam(value = "page", defaultValue = "0") Integer page,
          @RequestParam(value = "size", defaultValue = "20") Integer size) {
    List<ReplyDTO> replies = service.findAllByAuthor(userId, PageRequest.of(page, size));
     return ResponseEntity.ok().body(replies);
  }

  @GetMapping("/post/{postId}")
  public ResponseEntity<List<ReplyDTO>> getRepliesOfPost(
          @PathVariable Long postId,
          @RequestParam(value = "page", defaultValue = "0") Integer page,
          @RequestParam(value = "size", defaultValue = "20") Integer size) {
    List<ReplyDTO> replies = service.findAllByPost(postId, PageRequest.of(page, size));
    return ResponseEntity.ok().body(replies);
  }

  @PostMapping("/reply-to/{postId}")
  public ResponseEntity<ReplyDTO> createReply(@RequestBody Reply reply, @PathVariable Long postId, Authentication auth, @AuthenticationPrincipal UserDetails activeUser) {
    Long userId = userRepository.findByUsername(activeUser.getUsername()).get().getId();
    Reply newReply = service.addReply(reply, userId, postId);

    if (userId.equals(newReply.getAuthor().getId())) {
      URI uri = ServletUriComponentsBuilder
              .fromCurrentRequest()
              .path("/{userId}")
              .buildAndExpand(newReply.getId())
              .toUri();

      ReplyDTO replyDto = repository.findById(newReply.getId()).map(ReplyDTO::converter).get();

      return ResponseEntity.created(uri).body(replyDto);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ReplyDTO> updateReply(@RequestBody Reply reply, @PathVariable Long id, @AuthenticationPrincipal UserDetails activeUser) {
    Long userId = userRepository.findByUsername(activeUser.getUsername()).get().getId();
    Long replyId = repository.findById(id).get().getId();
    if (userId.equals(replyId)) {
      Reply updatedReply = service.updateReply(reply, id);
      ReplyDTO replyDto = repository.findById(updatedReply.getId()).map(ReplyDTO::converter).get();
      return ResponseEntity.ok().body(replyDto);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReply(@PathVariable Long id, @AuthenticationPrincipal UserDetails activeUser) {
    Long userId = userRepository.findByUsername(activeUser.getUsername()).get().getId();
    Long replyId = repository.findById(id).get().getId();
    if (userId.equals(replyId)) {
      service.deleteReply(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
