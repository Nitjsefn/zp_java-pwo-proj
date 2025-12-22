package com.csuni.pjp.client.services;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.csuni.pjp.client.models.AuthResponse;
import com.csuni.pjp.client.models.UserLoginDTO;
import com.csuni.pjp.client.models.UserRegisterDTO;
import com.csuni.pjp.client.support.IRequestSender;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebAuthGateway implements IWebAuthGateway {
    @NonNull
    private IRequestSender requestSender;
    private ObjectMapper objMapper = new ObjectMapper();
    @NonNull
    private Environment env;

    @Override
    public AuthResponse login(UserLoginDTO userLoginDTO) {
        String json;
        try {
            json = objMapper.writeValueAsString(userLoginDTO);
        }
        catch (Exception ex) {
            throw new LoginRequestCreationException();
        }

        String response = requestSender.auth(env.getProperty("apiv1.login"), "POST", json);
        AuthResponse authResponse;
        try {
            authResponse = objMapper.readValue(response, AuthResponse.class);
        }
        catch (Exception ex) {
            throw new InvalidLoginResponseException();
        }
        return authResponse;
    }

    @Override
    public AuthResponse register(UserRegisterDTO userRegisterDTO) {
        String json;
        try {
            json = objMapper.writeValueAsString(userRegisterDTO);
        }
        catch (Exception ex) {
            throw new RegisterRequestCreationException();
        }

        String response = requestSender.auth(env.getProperty("apiv1.register"), "POST", json);
        AuthResponse authResponse;
        try {
            authResponse = objMapper.readValue(response, AuthResponse.class);
        }
        catch (Exception ex) {
            throw new InvalidRegisterResponseException();
        }
        return authResponse;
    }

    @Override
    public void logout() {
        requestSender.clearCredentials();
    }
}
