package com.example.bankcards.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO запроса для перевода средств между двумя картами.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    @NotNull
    private Long fromCardId;

    @NotNull
    private Long toCardId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}
