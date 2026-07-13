package com.example.bankcards.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO ответа, содержащее JWT-токен после успешной аутентификации.
 */
@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;
}
