package com.com.csuni.pjp.server.authService;

import com.com.csuni.pjp.server.dto.LoginRequestDTO;
import com.com.csuni.pjp.server.dto.LoginAndRegisterResponseDTO;
import com.com.csuni.pjp.server.dto.RegisterRequestDTO;
import com.com.csuni.pjp.server.exception.ConflictException;
import com.com.csuni.pjp.server.exception.InvalidCredentialsException;
import com.com.csuni.pjp.server.exception.ValidationException;
import com.com.csuni.pjp.server.repository.UserRepository;

import com.com.csuni.pjp.server.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("integration_tests")
@Transactional


class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    RegisterRequestDTO createRegisterDTO(String username, String email, String password){
        RegisterRequestDTO req = new RegisterRequestDTO();
        req.username = username;
        req.email = email;
        req.password = password;
        return req;
    }

    @Test
    void registerUserSuccess() {
        assertDoesNotThrow(() ->
                authService.register(createRegisterDTO("test", "test@example.com", "password123")));
        assertTrue(userRepository.findByUsername("test").isPresent());
    }
    @Test
    void loginUserSuccess() {

        authService.register(createRegisterDTO("test", "test@example.com", "password123"));

        LoginRequestDTO log = new LoginRequestDTO();
        log.login = "test";
        log.password = "password123";
        LoginAndRegisterResponseDTO response = authService.login(log,null);

        assertNotNull(response);
        assertEquals("test", response.username());
        assertEquals("test@example.com", response.email());
        assertNotNull(response.jwt());
        assertFalse(response.jwt().isBlank());
    }
    @Test
    void loginUserFail() {

        LoginRequestDTO log = new LoginRequestDTO();
        log.login = "test";
        log.password = "password123";

        assertThrows(InvalidCredentialsException.class, () -> authService.login(log,null));
    }

    @Test
    @DisplayName("User registration fails due to the invalid username")
    void registerUserFailInvalidUsername() {
        ValidationException ex = assertThrows(ValidationException.class, () ->
                authService.register(createRegisterDTO("test@", "test@example.com", "password123"))
        );
        assertEquals("INVALID_USERNAME", ex.getCode());
    }

    @Test
    @DisplayName("User registration fails due to the empty username")
    void registerUserFailEmptyUsername() {
        ValidationException ex = assertThrows(ValidationException.class, () ->
                authService.register(createRegisterDTO("", "test@example.com", "password123"))
        );
        assertEquals("INVALID_USERNAME", ex.getCode());
    }

    @Test
    @DisplayName("User registration fails due to the invalid email")
    void registerUserFailInvalidEmail() {
        ValidationException ex = assertThrows(ValidationException.class, () ->
                authService.register(createRegisterDTO("test", "test", "password123"))
        );
        assertEquals("INVALID_EMAIL", ex.getCode());
    }

    @Test
    @DisplayName("User registration fails due to the invalid password")
    void registerUserFailInvalidPassword() {
        ValidationException ex = assertThrows(ValidationException.class, () ->
                authService.register(createRegisterDTO("test", "test@example.com", "pass"))
        );
        assertEquals("INVALID_PASSWORD", ex.getCode());
    }

    @Test
    @DisplayName("User registration fails due to the duplicate username")
    void registerUserFailDuplicateUsername() {
        authService.register(createRegisterDTO("test", "test1@example.com", "password123"));

        ConflictException ex = assertThrows(ConflictException.class, () ->
                authService.register(createRegisterDTO("test", "test2@example.com", "password123"))
        );
        assertEquals("USERNAME_ALREADY_EXISTS", ex.getCode());
    }

    @Test
    @DisplayName("User registration fails due to the duplicate email")
    void registerUserFailDuplicateEmail() {
        authService.register(createRegisterDTO("test1", "test@example.com", "password123"));

        ConflictException ex = assertThrows(ConflictException.class, () ->
                authService.register(createRegisterDTO("test2", "test@example.com", "password123"))
        );
        assertEquals("EMAIL_ALREADY_EXISTS", ex.getCode());
    }

}