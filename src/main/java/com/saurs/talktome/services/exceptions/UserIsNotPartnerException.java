package com.saurs.talktome.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UserIsNotPartnerException extends RuntimeException {
  
  public UserIsNotPartnerException() {
    super("User is not allowed to send messages to a non-partner user.");
  }
}
