package com.smartparking.dto;

import com.smartparking.enums.VendorBillStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorBillResponse {
    private Long id;
    private String billNumber;
    private Long purchaseOrderId;
    private Long vendorId;
    private String vendorName;
    private BigDecimal amount;
    private LocalDateTime billDate;
    private VendorBillStatus status;
}
