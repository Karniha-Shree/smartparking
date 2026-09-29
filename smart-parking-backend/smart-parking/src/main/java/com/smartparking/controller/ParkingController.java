package com.smartparking.controller;

import com.smartparking.dto.*;
import com.smartparking.entity.ParkingSession;
import com.smartparking.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/entry")
    public ResponseEntity<ParkingSession> vehicleEntry(@Valid @RequestBody ParkingEntryRequest request) {
        return new ResponseEntity<>(parkingService.vehicleEntry(request), HttpStatus.CREATED);
    }

    @PutMapping("/exit/{id}")
    public ResponseEntity<ParkingExitResponse> vehicleExit(@PathVariable Long id) {
        return ResponseEntity.ok(parkingService.vehicleExit(id));
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<ParkingSlotResponse>> getAvailableSlots() {
        return ResponseEntity.ok(parkingService.getAvailableSlots());
    }

    @GetMapping("/session/{id}")
    public ResponseEntity<ParkingSession> getSession(@PathVariable Long id) {
        return ResponseEntity.ok(parkingService.getSession(id));
    }
}
