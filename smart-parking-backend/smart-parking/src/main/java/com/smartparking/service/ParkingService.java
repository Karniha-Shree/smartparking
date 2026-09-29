package com.smartparking.service;

import com.smartparking.dto.ParkingEntryRequest;
import com.smartparking.dto.ParkingExitResponse;
import com.smartparking.entity.ParkingSession;

import java.util.List;

public interface ParkingService {
    ParkingSession vehicleEntry(ParkingEntryRequest request);
    ParkingExitResponse vehicleExit(Long sessionId);
    ParkingSession getSession(Long sessionId);
    List<com.smartparking.dto.ParkingSlotResponse> getAvailableSlots();
}
