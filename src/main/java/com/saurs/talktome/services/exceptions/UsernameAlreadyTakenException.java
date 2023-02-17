package com.saurs.talktome.services.exceptions;

public class UsernameAlreadyTakenException extends RuntimeException {

    public UsernameAlreadyTakenException() {
        super("This username is already taken.");
    }
}
