package com.smartparking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class PricingRuleRequest {

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private BigDecimal rate;

    // Leave both null for an "all day" rule.
    private LocalTime startTime;

    private LocalTime endTime;

    private boolean peakHour;

    private boolean active = true;
}
