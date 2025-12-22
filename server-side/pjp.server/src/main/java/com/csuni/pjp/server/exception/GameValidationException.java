package com.csuni.pjp.serv.exception;

public class GameValidationException extends ValidationException {
    public GameValidationException(String code, String message) {
        super(code, message);
    }
}