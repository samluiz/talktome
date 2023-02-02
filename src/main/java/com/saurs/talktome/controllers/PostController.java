package com.saurs.talktome.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.saurs.talktome.dtos.PostDTO;
import com.saurs.talktome.models.Post;
import com.saurs.talktome.repositories.PostRepository;
import com.saurs.talktome.services.PostService;

@RestController
@RequestMapping(value = "/api/posts")
public class PostController {
  
  @Autowired
  private PostService service;

  @Autowired
  private PostRepository repository;

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

  @GetMapping("/sent/user/{userId}")
  public ResponseEntity<List<PostDTO>> getPostsFromUser(@PathVariable Long userId,
                                                           @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                           @RequestParam(value = "size", defaultValue = "20") Integer size) {
    List<PostDTO> posts = service.findAllByAuthor(userId, PageRequest.of(page, size));
     return ResponseEntity.ok().body(posts);
  }

  @PostMapping("/user/{userId}/post")
  public ResponseEntity<PostDTO> createPost(@RequestBody Post post, @PathVariable Long userId) {
    Post newPost = service.addPost(post, userId);
    URI uri = ServletUriComponentsBuilder
    .fromCurrentRequest()
    .path("/{userId}")
    .buildAndExpand(newPost.getId())
    .toUri();

    PostDTO msgDto = repository.findById(newPost.getId()).map(PostDTO::converter).get();

    return ResponseEntity.created(uri).body(msgDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PostDTO> updatePost(@RequestBody Post post, @PathVariable Long id) {
    Post updatedPost = service.updatePost(post, id);

    PostDTO msgDto = repository.findById(updatedPost.getId()).map(PostDTO::converter).get();
    return ResponseEntity.ok().body(msgDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePost(@PathVariable Long id) {
    service.deletePost(id);
    return ResponseEntity.noContent().build();
  }
}
