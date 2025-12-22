package com.csuni.pjp.client.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    @Getter @Setter
    private String error = null;
    @Getter @Setter
    private String message = null;
    @Getter @Setter
    private String username = null;
    @Getter @Setter
    private String email = null;
    @Getter @Setter
    private String jwt = null;
}
