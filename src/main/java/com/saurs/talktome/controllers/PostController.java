package com.saurs.talktome.controllers;

import com.saurs.talktome.dtos.PostDTO;
import com.saurs.talktome.models.Post;
import com.saurs.talktome.repositories.PostRepository;
import com.saurs.talktome.repositories.UserRepository;
import com.saurs.talktome.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/posts")
public class PostController {
  
  @Autowired
  private PostService service;

  @Autowired
  private PostRepository repository;

  @Autowired
  private UserRepository userRepository;

  @GetMapping
  public ResponseEntity<List<PostDTO>> getAllPosts(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "20") Integer size) {
    List<PostDTO> posts = service.findAll(PageRequest.of(page, size));
    return ResponseEntity.ok().body(posts);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
    PostDTO post = service.findById(id);
     if (post == null) {
      return ResponseEntity.notFound().build();
     }
     return ResponseEntity.ok().body(post);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<PostDTO>> getPostsFromUser(
          @PathVariable Long userId,
          @RequestParam(value = "page", defaultValue = "0") Integer page,
          @RequestParam(value = "size", defaultValue = "20") Integer size) {
    List<PostDTO> posts = service.findAllByAuthor(userId, PageRequest.of(page, size));
     return ResponseEntity.ok().body(posts);
  }

  @PostMapping("/new")
  public ResponseEntity<PostDTO> createPost(@RequestBody Post post, @AuthenticationPrincipal UserDetails activeUser) {
    Long userId = userRepository.findByUsername(activeUser.getUsername()).get().getId();
    Post newPost = service.addPost(post, userId);
    if (userId.equals(post.getAuthor().getId())) {
      URI uri = ServletUriComponentsBuilder
              .fromCurrentRequest()
              .path("/{userId}")
              .buildAndExpand(newPost.getId())
              .toUri();
      PostDTO postDto = repository.findById(newPost.getId()).map(PostDTO::converter).get();
      return ResponseEntity.created(uri).body(postDto);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<PostDTO> updatePost(@RequestBody Post post, @PathVariable Long id, @AuthenticationPrincipal UserDetails activeUser) {
    Long userId = userRepository.findByUsername(activeUser.getUsername()).get().getId();
    if (userId.equals(post.getAuthor().getId())) {
      Post updatedPost = service.updatePost(post, id);
      PostDTO postDto = repository.findById(updatedPost.getId()).map(PostDTO::converter).get();
      return ResponseEntity.ok().body(postDto);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails activeUser) {
    Long userId = userRepository.findByUsername(activeUser.getUsername()).get().getId();
    Long postId = repository.findById(id).get().getId();
    if (userId.equals(postId)) {
      service.deletePost(id);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }
}
