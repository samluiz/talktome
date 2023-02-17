package com.saurs.talktome.services.exceptions;


public class UsernameNotFoundException extends RuntimeException {

  public UsernameNotFoundException(String username) {
    super("Object not found. Username: " + username);
  }
  
}