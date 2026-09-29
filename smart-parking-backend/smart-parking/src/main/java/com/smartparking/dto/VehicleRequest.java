package com.smartparking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleRequest {

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;

    private String vehicleType;

    @NotNull(message = "customerId is required")
    private Long customerId;
}
