package com.saurs.talktome.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.saurs.talktome.dtos.PostDTO;
import com.saurs.talktome.models.Post;
import com.saurs.talktome.models.User;
import com.saurs.talktome.repositories.PostRepository;
import com.saurs.talktome.repositories.UserRepository;
import com.saurs.talktome.services.exceptions.DatabaseException;
import com.saurs.talktome.services.exceptions.ObjectNotFoundException;
import com.saurs.talktome.utils.ServiceUtils;

@Service
public class PostService {

  @Autowired
  private PostRepository repository;

  @Autowired
  private UserRepository userRepository;

  public List<PostDTO> findAll(Pageable pageable) {
    return repository.findAll(pageable).stream().map(PostDTO::converter).collect(Collectors.toList());
  }

  public PostDTO findById(Long id) {
    Optional<PostDTO> getPost = repository.findById(id).map(PostDTO::converter);
    return getPost.orElseThrow(() -> new ObjectNotFoundException(id));
  }

  public List<PostDTO> findAllByAuthor(Long id, Pageable pageable) {
    return repository.findAllByAuthorId(id, pageable).stream().map(PostDTO::converter).collect(Collectors.toList());
  }

  public Post addPost(Post obj, Long authorId) {
    User author = userRepository.findById(authorId).orElseThrow(() -> new ObjectNotFoundException(authorId));
    author.getPosts().add(obj);
    obj.setAuthor(author);
    return repository.save(obj);
  }

  public Post updatePost(Post obj, Long id) {
    Optional<Post> oldPost = repository.findById(id);
    BeanUtils.copyProperties(obj, oldPost.orElseThrow(() -> new ObjectNotFoundException(id)), ServiceUtils.getNullPropertyNames(obj));
    return repository.save(oldPost.get());
  }

  public void deletePost(Long id) {
    try {
      repository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
        throw new DatabaseException(e.getCause());
    }
  }
  
}
