package com.csuni.pjp.client.support;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
public class AppUser {
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String email;


    public void clear() {
        username = null;
        email = null;
    }

    public boolean isLoggedIn() {
        if(username == null || email == null) {
            return false;
        }
        return true;
    }
}
