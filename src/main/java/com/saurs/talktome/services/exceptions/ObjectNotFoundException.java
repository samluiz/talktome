package com.saurs.talktome.services.exceptions;


public class ObjectNotFoundException extends RuntimeException {

  public ObjectNotFoundException(Object id) {
    super("Object not found. ID: " + id);
  }
  
}