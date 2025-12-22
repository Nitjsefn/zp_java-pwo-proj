package com.csuni.pjp.client.models;

public class GameModelDoesNotExistException extends RuntimeException {
    public GameModelDoesNotExistException() {
        this("Requested object does not exist or fetched prototype does not match");
    }

    public GameModelDoesNotExistException(String msg) {
        super(msg);
    }
}
