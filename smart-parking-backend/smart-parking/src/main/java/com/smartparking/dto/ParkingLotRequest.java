package com.smartparking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ParkingLotRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String location;

    @NotNull
    @Positive(message = "totalSlots must be positive")
    private Integer totalSlots;
}
