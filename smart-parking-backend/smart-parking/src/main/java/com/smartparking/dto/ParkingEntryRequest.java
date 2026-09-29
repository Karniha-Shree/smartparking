package com.smartparking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParkingEntryRequest {

    @NotNull(message = "customerId is required")
    private Long customerId;

    @NotNull(message = "vehicleId is required")
    private Long vehicleId;

    @NotNull(message = "parkingLotId is required")
    private Long parkingLotId;
}
