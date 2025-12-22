package com.csuni.pjp.client.models;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.csuni.pjp.client.services.ICredentialsValidationService;
import com.csuni.pjp.client.services.IWebAuthGateway;
import com.csuni.pjp.client.support.AppUser;
import com.csuni.pjp.client.support.UnsuccessfullWebResponseException;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class LoginModel {
    @NonNull
    private ICredentialsValidationService validator;
    @NonNull
    private IWebAuthGateway authGateway;
    @Getter @Setter
    private String login;
    @Getter @Setter
    private String password;
    @NonNull
    private AppUser appUser;


    public String loginAction() {
        String result = validate();
        if(result.length() > 0) {
            return result;
        }
        try {
            AuthResponse user = authGateway.login(new UserLoginDTO(login, password));
            appUser.setEmail(user.getEmail());
            appUser.setUsername(user.getUsername());
        }
        catch (UnsuccessfullWebResponseException ex) {
            return "Cannot login with those credentials";
        }
        return "";
    }

    public void logout() {
        authGateway.logout();
    }

    private String validate() {
        if(login.contains("@")) {
            String result = validator.validateEmail(login);
            if(result.length() > 0) {
                return result;
            }
        }
        else {
            String result = validator.validateUsername(login);
            if(result.length() > 0) {
                return result;
            }
        }

        String result = validator.validatePassword(password);
        return result;
    }
}
