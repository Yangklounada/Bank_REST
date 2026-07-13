package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;

/**
 * Выбрасывается, когда запрошенный ресурс (пользователь, карта и т.д.) не найден.
 * Маппится на HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
