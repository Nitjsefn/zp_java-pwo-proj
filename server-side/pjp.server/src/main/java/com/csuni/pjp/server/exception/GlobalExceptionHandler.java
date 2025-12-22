package com.csuni.pjp.serv.exception;

import com.csuni.pjp.serv.dto.ErrorResponseDTO;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class    GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflict(ConflictException ex) {
        return ResponseEntity.status(409).body(
                new ErrorResponseDTO(ex.getCode(), ex.getMessage())
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorized(InvalidCredentialsException ex) {
        return ResponseEntity.status(401).body(
                new ErrorResponseDTO("UNAUTHORIZED", ex.getMessage())
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(ValidationException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponseDTO(ex.getCode(), ex.getMessage())
        );
    }


}