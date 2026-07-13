package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;

/**
 * Выбрасывается при конфликте ресурсов (например, дубликат имени пользователя или email).
 * Маппится на HTTP 409 Conflict.
 */
public class DuplicateResourceException extends ApiException {
    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
