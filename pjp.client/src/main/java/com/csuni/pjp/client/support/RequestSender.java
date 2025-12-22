package com.csuni.pjp.client.support;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Component
@RequiredArgsConstructor
public class RequestSender implements IRequestSender {
    private String destination;
    private String token;
    @NonNull
    private IAuthTokenExtractor tokenExtractor;
    private HttpClient httpClient = HttpClient.newHttpClient();
    private final String tokenRegenEndpoint = "/api/v1/auth/login";
    private boolean requestLoopPreventing = false;
    @NonNull
    private AppUser appUser;
    @NonNull
    private Environment env;


    @Override
    public String auth(String resource, String method, String body) {
        String response = request(resource, method, body);
        AuthTokenExtractorResult result = tokenExtractor.extractAuthToken(response);
        token = result.getToken();
        return result.getSrc();
    }

    @Override
    public String request(String resource, String method, String body) {
        if(destination == null) {
            destination = env.getProperty("backend.destination");
        }
        System.out.println("Requested data:\n\t" + method + " " + resource);
        System.out.println("\t" + body);
        String uriStr = destination + resource;
        Builder reqBuilder;
        try {
            reqBuilder = HttpRequest.newBuilder(URI.create(uriStr));
        }
        catch (IllegalArgumentException ex) {
            System.out.println(uriStr);
            throw ex;
        }
        switch(method) {
            case "GET": reqBuilder.GET(); break;
            case "POST": reqBuilder.POST(HttpRequest.BodyPublishers.ofString(body)); break;
        }
        if(token != null) {
            reqBuilder.setHeader("Authorization", "Bearer " + token);
        }
        reqBuilder.setHeader("Content-Type", "application/json");
        reqBuilder.setHeader("Accept", "*/*");
        HttpRequest req = reqBuilder.build();
        HttpResponse<String> httpResponse;
        try {
            httpResponse = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception ex) {
            throw new HttpSendException();
        }
        if(!requestLoopPreventing && httpResponse.statusCode() == 401 && token != null) {
            requestLoopPreventing = true;
            auth(tokenRegenEndpoint, "POST", "{}");
            requestLoopPreventing = false;
        }
        else if(requestLoopPreventing && httpResponse.statusCode() == 401) {
            clearCredentials();
        }

        if(httpResponse.statusCode() < 200 || httpResponse.statusCode() >= 300) {
            throw new UnsuccessfullWebResponseException("HTTP Response Status code: " + httpResponse.statusCode());
        }
        String response = httpResponse.body();
        return response;
    }

    @Override
    public void clearCredentials() {
        token = null;
        appUser.clear();
    }
}
