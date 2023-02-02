package com.saurs.talktome.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saurs.talktome.models.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

  @Query("select p from Post p where p.author.id = ?1")
  List<Post> findAllByAuthorId(Long userId, Pageable pageable);
}
