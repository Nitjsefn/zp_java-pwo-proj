package com.csuni.pjp.client.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class JWTContent {
    @Getter @Setter
    private String username;
    @Getter @Setter
    private Date expirationDate;
}
