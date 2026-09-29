package com.smartparking.service;

import com.smartparking.dto.ParkingLotRequest;
import com.smartparking.dto.ParkingLotResponse;
import com.smartparking.dto.ParkingSlotResponse;

import java.util.List;

public interface ParkingLotService {
    ParkingLotResponse create(ParkingLotRequest request);
    List<ParkingLotResponse> getAll();
    ParkingLotResponse getById(Long id);
    ParkingLotResponse update(Long id, ParkingLotRequest request);
    void delete(Long id);

    // Generates ParkingSlot rows (A01, A02, ...) up to totalSlots for a lot.
    List<ParkingSlotResponse> getSlotsForLot(Long lotId);
}
