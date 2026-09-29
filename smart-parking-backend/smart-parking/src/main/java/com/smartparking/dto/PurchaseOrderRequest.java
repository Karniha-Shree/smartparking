package com.smartparking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseOrderRequest {

    @NotNull(message = "vendorId is required")
    private Long vendorId;

    @NotBlank(message = "description is required")
    private String description;

    @NotNull
    @Positive(message = "amount must be positive")
    private BigDecimal amount;
}
