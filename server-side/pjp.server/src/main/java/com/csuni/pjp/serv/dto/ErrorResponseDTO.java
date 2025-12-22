package com.csuni.pjp.serv.dto;

public record ErrorResponseDTO(
        String error,
        String message
) {}