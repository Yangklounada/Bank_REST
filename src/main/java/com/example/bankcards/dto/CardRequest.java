package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class CardRequest {
    @NotBlank
    private String cardNumber;

    @NotBlank
    private String cardHolder;

    @NotBlank
    private String expiryDate;

    private BigDecimal initialBalance;

    public CardRequest(String cardNumber, String cardHolder, String expiryDate, BigDecimal initialBalance) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.initialBalance = initialBalance;
    }

    public CardRequest() {

    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
