package com.saurs.talktome.controllers;

import com.saurs.talktome.dtos.PostDTO;
import com.saurs.talktome.dtos.ReplyDTO;
import com.saurs.talktome.models.Post;
import com.saurs.talktome.models.Reply;
import com.saurs.talktome.repositories.PostRepository;
import com.saurs.talktome.repositories.ReplyRepository;
import com.saurs.talktome.services.PostService;
import com.saurs.talktome.services.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/replies")
public class ReplyController {
  
  @Autowired
  private ReplyService service;

  @Autowired
  private ReplyRepository repository;

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
  public ResponseEntity<ReplyDTO> createReply(@RequestBody Reply reply, @PathVariable Long postId) {
    // TODO get authenticated user
    Reply newReply = service.addReply(reply, userId, postId);
    URI uri = ServletUriComponentsBuilder
    .fromCurrentRequest()
    .path("/{userId}")
    .buildAndExpand(newReply.getId())
    .toUri();

    ReplyDTO replyDto = repository.findById(newReply.getId()).map(ReplyDTO::converter).get();

    return ResponseEntity.created(uri).body(replyDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ReplyDTO> updateReply(@RequestBody Reply reply, @PathVariable Long id) {
    // TODO get authenticated user and check if it's him

    Reply updatedReply = service.updateReply(reply, id);

    ReplyDTO replyDto = repository.findById(updatedReply.getId()).map(ReplyDTO::converter).get();
    return ResponseEntity.ok().body(replyDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReply(@PathVariable Long id) {
    // TODO get authenticated user and check if it's him

    service.deleteReply(id);
    return ResponseEntity.noContent().build();
  }
}
