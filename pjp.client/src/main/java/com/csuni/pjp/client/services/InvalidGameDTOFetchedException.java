package com.csuni.pjp.client.services;

public class InvalidGameDTOFetchedException extends RuntimeException {
    public InvalidGameDTOFetchedException() {
        super();
    }

    public InvalidGameDTOFetchedException(String msg) {
        super(msg);
    }
}

