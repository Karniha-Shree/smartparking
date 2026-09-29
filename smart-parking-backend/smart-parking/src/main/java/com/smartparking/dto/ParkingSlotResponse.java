package com.smartparking.dto;

import com.smartparking.enums.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlotResponse {
    private Long id;
    private String slotNumber;
    private SlotStatus status;
    private Long parkingLotId;
}
