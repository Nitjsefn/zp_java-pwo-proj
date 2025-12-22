package com.csuni.pjp.serv.controller;

import com.csuni.pjp.serv.dto.LoginAndRegisterResponseDTO;
import com.csuni.pjp.serv.dto.LoginRequestDTO;
import com.csuni.pjp.serv.dto.RegisterRequestDTO;
import com.csuni.pjp.serv.service.AuthService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public LoginAndRegisterResponseDTO register(@RequestBody RegisterRequestDTO request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginAndRegisterResponseDTO login(
            @RequestBody LoginRequestDTO request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        return authService.login(request, authHeader);
    }


    @GetMapping("/home")
    public String home() {
        return "home endpoint";
    }

    @GetMapping("/game")
    public String game() {
        return "GL HF";
    }

}