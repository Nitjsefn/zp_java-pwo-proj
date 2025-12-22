package com.csuni.pjp.client.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class AuthTokenExtractorResult {
    @Getter @Setter
    private String token;
    @Getter @Setter
    private String src;
}
