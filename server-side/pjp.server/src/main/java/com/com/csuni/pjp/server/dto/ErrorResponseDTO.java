package com.com.csuni.pjp.server.dto;

public record ErrorResponseDTO(
        String error,
        String message
) {}