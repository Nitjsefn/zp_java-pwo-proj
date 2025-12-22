package com.csuni.pjp.client.services;

public class LoginRequestCreationException extends RuntimeException {
    public LoginRequestCreationException() {
        super();
    }

    public LoginRequestCreationException(String msg) {
        super(msg);
    }
}
