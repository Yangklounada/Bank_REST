package com.example.bankcards.util;

public class CardMaskUtil {
    private CardMaskUtil() {};

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
