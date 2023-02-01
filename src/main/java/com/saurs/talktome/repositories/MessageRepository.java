package com.saurs.talktome.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saurs.talktome.models.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

  @Query("select m from Message m where m.fromUser.id = ?1")
  List<Message> findAllByFromUserId(Long userId);

  @Query("select m from Message m where m.toUser.id = ?1")
  List<Message> findAllByToUserId(Long userId);
}
