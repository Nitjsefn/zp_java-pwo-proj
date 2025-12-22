package com.csuni.pjp.client.models;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.csuni.pjp.client.services.ICredentialsValidationService;
import com.csuni.pjp.client.services.IWebAuthGateway;
import com.csuni.pjp.client.support.UnsuccessfullWebResponseException;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class RegisterModel {
    @NonNull
    private ICredentialsValidationService validator;
    @NonNull
    private IWebAuthGateway authGateway;
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String password;


    public String registerAction() {
        String result = validate();
        if(result.length() > 0) {
            return result;
        }
        try {
            authGateway.register(new UserRegisterDTO(username, email, password));
        }
        catch (UnsuccessfullWebResponseException ex) {
            System.out.println(ex.getMessage());
            return "Cannot register with those credentials";
        }
        return "";
    }

    private String validate() {
        String result = validator.validateUsername(username);
        if(result.length() > 0) {
            return result;
        }
        result = validator.validateEmail(email);
        if(result.length() > 0) {
            return result;
        }

        result = validator.validatePassword(password);
        return result;
    }
}

