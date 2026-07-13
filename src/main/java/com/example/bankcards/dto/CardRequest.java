package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO запроса для создания новой банковской карты.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {

    @NotBlank
    private String cardNumber;

    @NotBlank
    private String cardHolder;

    @NotBlank
    private String expiryDate;

    private BigDecimal initialBalance;
}
