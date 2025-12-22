package com.com.csuni.pjp.server.exception;

public class GameValidationException extends ValidationException {
    public GameValidationException(String code, String message) {
        super(code, message);
    }
}