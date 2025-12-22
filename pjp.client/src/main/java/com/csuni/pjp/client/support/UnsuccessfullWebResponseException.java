package com.csuni.pjp.client.support;

public class UnsuccessfullWebResponseException extends RuntimeException {
    public UnsuccessfullWebResponseException() {
        super();
    }

    public UnsuccessfullWebResponseException(String msg) {
        super(msg);
    }
}
