package com.smartparking.dto;

import com.smartparking.enums.PurchaseOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderResponse {
    private Long id;
    private String poNumber;
    private Long vendorId;
    private String vendorName;
    private String description;
    private BigDecimal amount;
    private PurchaseOrderStatus status;
    private LocalDateTime orderDate;
}
