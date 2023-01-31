package com.saurs.talktome.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.saurs.talktome.models.User;
import com.saurs.talktome.models.enums.Gender;
import com.saurs.talktome.services.UserService;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

  @Autowired
  private UserService userService;

  @Override
  public void run(String... args) throws Exception {
    User u1 = new User(null, "samuel@mail.com", "86999999999", "09x89", "Samuel", "Luiz", Gender.MALE, null, null, null, null);

    userService.addUser(u1);

    User u2 = new User(null, "luiza@mail.com", "77999999999", "7x918", "Luiza", "Franca", Gender.FEMALE, null, null, null, null);

    userService.addUser(u2);

    userService.setPartner(u1.getId(), u2.getId());
  }
  
}
