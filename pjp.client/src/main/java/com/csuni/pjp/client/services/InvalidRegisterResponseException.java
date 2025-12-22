package com.csuni.pjp.client.services;

public class InvalidRegisterResponseException extends RuntimeException {
    public InvalidRegisterResponseException() {
        super();
    }

    public InvalidRegisterResponseException(String msg) {
        super(msg);
    }
}

