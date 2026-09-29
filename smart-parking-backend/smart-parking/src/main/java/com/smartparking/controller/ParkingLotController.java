package com.smartparking.controller;

import com.smartparking.dto.ParkingLotRequest;
import com.smartparking.dto.ParkingLotResponse;
import com.smartparking.dto.ParkingSlotResponse;
import com.smartparking.service.ParkingLotService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking/lots")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @PostMapping
    public ResponseEntity<ParkingLotResponse> create(@Valid @RequestBody ParkingLotRequest request) {
        return new ResponseEntity<>(parkingLotService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ParkingLotResponse>> getAll() {
        return ResponseEntity.ok(parkingLotService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingLotResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(parkingLotService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingLotResponse> update(@PathVariable Long id, @Valid @RequestBody ParkingLotRequest request) {
        return ResponseEntity.ok(parkingLotService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        parkingLotService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Extra convenience endpoint: list all slots (with status) belonging to one lot.
    @GetMapping("/{id}/slots")
    public ResponseEntity<List<ParkingSlotResponse>> getSlots(@PathVariable Long id) {
        return ResponseEntity.ok(parkingLotService.getSlotsForLot(id));
    }
}
