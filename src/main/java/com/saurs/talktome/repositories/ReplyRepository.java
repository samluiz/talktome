package com.saurs.talktome.repositories;

import com.saurs.talktome.models.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.saurs.talktome.models.Reply;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r where r.author.id = ?1")
    List<Reply> findAllByAuthorId(Long userId, Pageable pageable);

    @Query("select r from Reply r where r.replyTo.id = ?1")
    List<Reply> findAllByPostId(Long postId, Pageable pageable);
  
}
