package com.smartparking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingRuleResponse {
    private Long id;
    private String name;
    private BigDecimal rate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean peakHour;
    private boolean active;
}
