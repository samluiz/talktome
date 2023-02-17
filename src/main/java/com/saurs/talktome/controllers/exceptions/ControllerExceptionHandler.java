package com.saurs.talktome.controllers.exceptions;

import java.time.Instant;

import com.saurs.talktome.services.exceptions.UsernameAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.saurs.talktome.services.exceptions.DatabaseException;
import com.saurs.talktome.services.exceptions.ObjectNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
  
  @ExceptionHandler(ObjectNotFoundException.class)
  public ResponseEntity<StandardError> resourceNotFound(ObjectNotFoundException e, HttpServletRequest request) {

    String error = "Object not found";

    HttpStatus status = HttpStatus.NOT_FOUND;

    StandardError err = new StandardError(
            Instant.now(),
            status.value(),
            error,
            e.getMessage(),
            request.getRequestURI());

    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<StandardError> DatabaseError(DatabaseException e, HttpServletRequest request) {

    String error = "Database error";

    HttpStatus status = HttpStatus.BAD_REQUEST;
    
    StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());

    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(UsernameAlreadyTakenException.class)
  public ResponseEntity<StandardError> UserAlreadyExists(UsernameAlreadyTakenException e, HttpServletRequest request) {

    String error = "Username already taken.";

    HttpStatus status = HttpStatus.CONFLICT;

    StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());

    return ResponseEntity.status(status).body(err);
  }

}