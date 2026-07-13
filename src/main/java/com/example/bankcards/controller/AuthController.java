package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthResponse;
import com.example.bankcards.dto.LoginRequest;
import com.example.bankcards.dto.RegisterRequest;
import com.example.bankcards.security.JwtUtils;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Обрабатывает регистрацию и вход пользователей. Выдаёт JWT-токены после успешной аутентификации.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Регистрирует нового пользователя и возвращает JWT-токен.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        userService.registerUser(request);
        String token = jwtUtils.generateToken(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Аутентифицирует существующего пользователя и возвращает JWT-токен.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String token = jwtUtils.generateToken(authentication.getName());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
