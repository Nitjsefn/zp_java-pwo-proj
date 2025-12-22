package com.com.csuni.pjp.server.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        this("Invalid login credentials");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
