package com.csuni.pjp.client.support;

import org.springframework.stereotype.Component;

import com.csuni.pjp.client.models.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtExtractor implements IAuthTokenExtractor {
    @Override
    public AuthTokenExtractorResult extractAuthToken(String src) {
        ObjectMapper objMapper = new ObjectMapper();
        AuthResponse response;
        String token;
        String responseStr;
        try {
            response = objMapper.readValue(src, AuthResponse.class);
            token = response.getJwt();
            response.setJwt(null);
            responseStr = objMapper.writeValueAsString(response);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new InvalidResponseException("Auth response does not match type AuthResponse");
        }
        return new AuthTokenExtractorResult(token, responseStr);
    }
}
