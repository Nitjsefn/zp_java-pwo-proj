package com.csuni.pjp.client.support;

public interface IRequestSender {
    String auth(String resource, String method, String body);
    String request(String resource, String method, String body);
    void clearCredentials();
}
