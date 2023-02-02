package com.saurs.talktome.services.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("This email was found in our database. Try logging in or create a account with a new email.");
    }
}
