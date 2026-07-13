package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;

/**
 * Базовое исключение для всех ошибок уровня API, содержащее HTTP-статус.
 */
public abstract class ApiException extends RuntimeException {

    private final HttpStatus status;

    protected ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
