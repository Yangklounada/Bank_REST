package com.example.bankcards.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO ответа, содержащее детали карты с маскированным номером.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {

    private Long id;
    private String maskedNumber;
    private String cardHolder;
    private LocalDate expiryDate;
    private String status;
    private BigDecimal balance;
    private Long ownerId;
}
