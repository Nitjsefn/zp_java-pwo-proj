package com.csuni.pjp.serv.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        this("Invalid login credentials");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
