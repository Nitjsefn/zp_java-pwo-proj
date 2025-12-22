package com.csuni.pjp.client.support;

public class InvalidResponseException extends RuntimeException {
    public InvalidResponseException() {
        super();
    }

    public InvalidResponseException(String msg) {
        super(msg);
    }
}
