package com.smartparking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {
    private Long id;
    private String vehicleNumber;
    private String vehicleType;
    private Long customerId;
    private String customerName;
}
