package com.saurs.talktome.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saurs.talktome.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
  
}
