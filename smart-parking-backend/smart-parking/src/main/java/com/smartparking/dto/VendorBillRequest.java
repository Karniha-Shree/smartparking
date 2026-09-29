package com.smartparking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VendorBillRequest {

    @NotNull(message = "purchaseOrderId is required")
    private Long purchaseOrderId;
}
