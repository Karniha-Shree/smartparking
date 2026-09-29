package com.smartparking.dto;

import com.smartparking.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotNull(message = "invoiceId is required")
    private Long invoiceId;

    @NotNull
    @Positive(message = "amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "paymentMethod is required")
    private PaymentMethod paymentMethod;
}
