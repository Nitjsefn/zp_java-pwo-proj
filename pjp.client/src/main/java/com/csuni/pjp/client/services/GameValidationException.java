package com.csuni.pjp.client.services;

public class GameValidationException extends RuntimeException {
    private final String code;

    public GameValidationException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}

