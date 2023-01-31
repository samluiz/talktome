package com.saurs.talktome.services;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.saurs.talktome.ApplicationConfigTest;
import com.saurs.talktome.repositories.UserRepository;

@DisplayName("UserServiceTest")
public class UserServiceTest extends ApplicationConfigTest {
  
  @MockBean
  private UserRepository repository;

  @Autowired
  private UserService service;

  
}
