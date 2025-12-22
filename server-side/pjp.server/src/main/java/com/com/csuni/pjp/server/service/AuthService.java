package com.com.csuni.pjp.server.service;

import com.com.csuni.pjp.server.dto.LoginAndRegisterResponseDTO;
import com.com.csuni.pjp.server.dto.LoginRequestDTO;
import com.com.csuni.pjp.server.dto.RegisterRequestDTO;
import com.com.csuni.pjp.server.exception.ConflictException;
import com.com.csuni.pjp.server.exception.InvalidCredentialsException;
import com.com.csuni.pjp.server.exception.ValidationException;
import com.com.csuni.pjp.server.model.auth.User;
import com.com.csuni.pjp.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public LoginAndRegisterResponseDTO register(RegisterRequestDTO request) {
        validateUsername(request.username);
        validateEmail(request.email);
        validatePassword(request.password);
        checkDuplicates(request.username, request.email);

        User user = new User();
        user.setUsername(request.username);
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());

        return new LoginAndRegisterResponseDTO(
                user.getUsername(),
                user.getEmail(),
                token
        );
    }

    public LoginAndRegisterResponseDTO login(LoginRequestDTO request,
                                             @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if ((isBlank(request.login) || isBlank(request.password)) && authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = null;
            boolean valid = false;

            try {
                username = jwtService.extractUsername(token);
                valid = jwtService.validateToken(token, username);
            } catch (io.jsonwebtoken.ExpiredJwtException e) {

                username = e.getClaims().getSubject();
            } catch (Exception e) {
                throw new InvalidCredentialsException();
            }

            if (valid || jwtService.isTokenWithinGracePeriod(token)) {
                User user = findUserByLogin(username);
                String newToken = jwtService.generateToken(user.getUsername());

                return new LoginAndRegisterResponseDTO(
                        user.getUsername(),
                        user.getEmail(),
                        newToken
                );
            } else {
                throw new InvalidCredentialsException();
            }
        }

        if (isBlank(request.login) || isBlank(request.password)) {
            throw new InvalidCredentialsException();
        }

        User user = findUserByLogin(request.login);

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user.getUsername());

        return new LoginAndRegisterResponseDTO(
                user.getUsername(),
                user.getEmail(),
                token
        );
    }

    private User findUserByLogin(String login) {
        return login.contains("@")
                ? userRepository.findByEmail(login)
                .orElseThrow(InvalidCredentialsException::new)
                : userRepository.findByUsername(login)
                .orElseThrow(InvalidCredentialsException::new);
    }

    private void validateUsername(String username) {
        if (isBlank(username)) {
            throwValidation("INVALID_USERNAME", "Username cannot be null or blank!");
        }
        if (username.contains("@")) {
            throwValidation("INVALID_USERNAME", "Username cannot contain '@'!");
        }
    }

    private void validateEmail(String email) {
        if (isBlank(email) || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throwValidation("INVALID_EMAIL", "Email format is invalid!");
        }
    }

    private void validatePassword(String password) {
        if (isBlank(password) || password.length() < 7) {
            throwValidation("INVALID_PASSWORD", "Password must be at least 8 characters!");
        }
    }

    private void checkDuplicates(String username, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throwConflict("USERNAME_ALREADY_EXISTS", "Username already taken!");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throwConflict("EMAIL_ALREADY_EXISTS", "Email already registered!");
        }
    }

    private void throwValidation(String code, String message) {
        throw new ValidationException(code, message);
    }

    private void throwConflict(String code, String message) {
        throw new ConflictException(code, message);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }


}