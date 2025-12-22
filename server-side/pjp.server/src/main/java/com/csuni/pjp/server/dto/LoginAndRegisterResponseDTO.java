package com.csuni.pjp.serv.dto;

public record LoginAndRegisterResponseDTO(
        String username,
        String email,
        String jwt
) {}