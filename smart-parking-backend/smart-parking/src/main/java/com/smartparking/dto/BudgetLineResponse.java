package com.smartparking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetLineResponse {
    private Long id;
    private Long accountId;
    private String accountName;
    private BigDecimal plannedAmount;
    private BigDecimal actualAmount;
    private BigDecimal variance;
}
