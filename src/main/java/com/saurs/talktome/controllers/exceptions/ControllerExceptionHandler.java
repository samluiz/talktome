package com.saurs.talktome.controllers.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.saurs.talktome.services.exceptions.AlreadySetException;
import com.saurs.talktome.services.exceptions.DatabaseException;
import com.saurs.talktome.services.exceptions.ObjectNotFoundException;
import com.saurs.talktome.services.exceptions.UserIsNotPartnerException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
  
  @ExceptionHandler(ObjectNotFoundException.class)
  public ResponseEntity<StandardError> resourceNotFound(ObjectNotFoundException e, HttpServletRequest request) {

    String error = "Object not found";

    HttpStatus status = HttpStatus.NOT_FOUND;

    StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());

    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<StandardError> DatabaseError(DatabaseException e, HttpServletRequest request) {

    String error = "Database error";

    HttpStatus status = HttpStatus.BAD_REQUEST;
    
    StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());

    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(AlreadySetException.class)
  public ResponseEntity<StandardError> alreadySet(AlreadySetException e, HttpServletRequest request) {
    String error = "Partner field error";

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    
    StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());

    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(UserIsNotPartnerException.class)
  public ResponseEntity<StandardError> notPartner(UserIsNotPartnerException e, HttpServletRequest request) {
    String error = "User is not partner";

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    
    StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());

    return ResponseEntity.status(status).body(err);
  }
}