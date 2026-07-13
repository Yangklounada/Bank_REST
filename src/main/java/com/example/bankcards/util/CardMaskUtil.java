package com.example.bankcards.util;

/**
 * Утилита для маскирования номеров карт, показывающая только последние четыре цифры.
 */
public class CardMaskUtil {
    private CardMaskUtil() {}

    /**
     * Маскирует номер карты, показывая только последние четыре цифры.
     * Возвращает "****", если входные данные null или слишком короткие.
     */
    public static String mask(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        String cleaned = cardNumber.replaceAll("\\D+", "");
        if (cleaned.length() < 4) {
            return "****";
        }
        String last4 = cleaned.substring(cleaned.length() - 4);
        String masked = "**** **** **** " + last4;
        return masked;
    }
}
