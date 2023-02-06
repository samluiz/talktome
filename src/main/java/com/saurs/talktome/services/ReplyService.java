package com.saurs.talktome.services;

import com.saurs.talktome.dtos.PostDTO;
import com.saurs.talktome.dtos.ReplyDTO;
import com.saurs.talktome.models.Post;
import com.saurs.talktome.models.Reply;
import com.saurs.talktome.models.User;
import com.saurs.talktome.repositories.PostRepository;
import com.saurs.talktome.repositories.UserRepository;
import com.saurs.talktome.services.exceptions.DatabaseException;
import com.saurs.talktome.services.exceptions.ObjectNotFoundException;
import com.saurs.talktome.utils.ServiceUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.saurs.talktome.repositories.ReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReplyService {
  
  @Autowired
  ReplyRepository repository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostRepository postRepository;

  public List<ReplyDTO> findAll (Pageable pageable) {
    return repository.findAll(pageable).stream().map(ReplyDTO::converter).collect(Collectors.toList());
  }

  public ReplyDTO findById(Long id) {
    Optional<ReplyDTO> getReply = repository.findById(id).map(ReplyDTO::converter);
    return getReply.orElseThrow(() -> new ObjectNotFoundException(id));
  }

  public List<ReplyDTO> findAllByAuthor(Long id, Pageable pageable) {
    return repository.findAllByAuthorId(id, pageable).stream().map(ReplyDTO::converter).collect(Collectors.toList());
  }

  public List<ReplyDTO> findAllByPost(Long id, Pageable pageable) {
    return repository.findAllByPostId(id, pageable).stream().map(ReplyDTO::converter).collect(Collectors.toList());
  }

  public Reply addReply(Reply obj, Long authorId, Long postId) {
    User author = userRepository.findById(authorId).orElseThrow(() -> new ObjectNotFoundException(authorId));
    Post post = postRepository.findById(postId).orElseThrow(() -> new ObjectNotFoundException(postId));
    post.getReplies().add(obj);
    obj.setAuthor(author);
    obj.setReplyTo(post);
    return repository.save(obj);
  }

  public Reply updateReply(Reply obj, Long id) {
    Optional<Reply> oldReply = repository.findById(id);
    BeanUtils.copyProperties(obj, oldReply.orElseThrow(() -> new ObjectNotFoundException(id)), ServiceUtils.getNullPropertyNames(obj));
    return repository.save(oldReply.get());
  }

  public void deleteReply(Long id) {
    try {
      repository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException(e.getCause());
    }
  }

}
