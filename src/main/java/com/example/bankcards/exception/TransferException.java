package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;

/**
 * Выбрасывается, когда валидация перевода не пройдена (та же карта, неактивна, недостаточно средств).
 * Маппится на HTTP 400 Bad Request.
 */
public class TransferException extends ApiException {
    public TransferException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
