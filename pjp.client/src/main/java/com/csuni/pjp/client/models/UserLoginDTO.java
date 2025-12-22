package com.csuni.pjp.client.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class UserLoginDTO {
    @Getter @Setter
    private String login;
    @Getter @Setter
    private String password;
}

