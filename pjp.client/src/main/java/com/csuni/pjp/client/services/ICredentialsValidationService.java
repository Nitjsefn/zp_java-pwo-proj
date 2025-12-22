package com.csuni.pjp.client.services;

public interface ICredentialsValidationService {
    String validateUsername(String username);
    String validateEmail(String email);
    String validatePassword(String password);
}
