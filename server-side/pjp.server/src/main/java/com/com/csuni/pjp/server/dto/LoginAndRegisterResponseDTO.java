package com.com.csuni.pjp.server.dto;

public record LoginAndRegisterResponseDTO(
        String username,
        String email,
        String jwt
) {}