package com.csuni.pjp.client.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class UserRegisterDTO {
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String password;
}
