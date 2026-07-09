package com.example.bankcards.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

public class CardResponse {
    private Long id;
    private String maskedNumber;
    private String cardHolder;
    private LocalDate expiryDate;
    private String status;
    private BigDecimal balance;
    private Long ownerId;

    public CardResponse(Long id, String maskedNumber, String cardHolder, LocalDate expiryDate, String status, BigDecimal balance, Long ownerId) {
        this.id = id;
        this.maskedNumber = maskedNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.status = status;
        this.balance = balance;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public String getMaskedNumber() {
        return maskedNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMaskedNumber(String maskedNumber) {
        this.maskedNumber = maskedNumber;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}