package com.saurs.talktome.services.exceptions;

public class AlreadySetException extends RuntimeException {

  public AlreadySetException() {
    super("User already have a partner. Remove the partner before trying to set a new one.");
  }
  
}