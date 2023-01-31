package com.saurs.talktome.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saurs.talktome.models.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
  
}
