package com.smartparking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLotResponse {
    private Long id;
    private String name;
    private String location;
    private Integer totalSlots;
}
