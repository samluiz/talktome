package com.saurs.talktome.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saurs.talktome.models.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
  
}
