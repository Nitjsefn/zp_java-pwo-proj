package com.csuni.pjp.client.services;

import org.springframework.stereotype.Component;

@Component
public class CredentialsValidationService implements ICredentialsValidationService {
    public String validateUsername(String username) {
        if(username == null) {
            return "Username cannot be empty";
        }
        if(username.contains("@")) {
            return "Username cannot contain '@' characters";
        }
        if(username.length() < 1) {
            return "Username is too short";
        }
        return "";
    }

    public String validateEmail(String email) {
        if(email == null) {
            return "Email cannot be empty";
        }
        if(!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return "Email is invalid";
        }
        return "";
    }

    public String validatePassword(String password) {
        if(password == null) {
            return "Password cannot be empty";
        }
        if(password.length() < 8) {
            return "Password is too short";
        }
        return "";
    }
}
