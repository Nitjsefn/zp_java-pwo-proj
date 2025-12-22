package com.csuni.pjp.client.services;

public class InvalidLoginResponseException extends RuntimeException {
    public InvalidLoginResponseException() {
        super();
    }

    public InvalidLoginResponseException(String msg) {
        super(msg);
    }
}

