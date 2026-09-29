package com.smartparking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetLineRequest {

    @NotNull(message = "accountId is required")
    private Long accountId;

    @NotNull
    @Positive(message = "plannedAmount must be positive")
    private BigDecimal plannedAmount;
}
