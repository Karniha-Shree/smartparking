package com.smartparking.service.impl;

import com.smartparking.dto.ParkingLotRequest;
import com.smartparking.dto.ParkingLotResponse;
import com.smartparking.dto.ParkingSlotResponse;
import com.smartparking.entity.ParkingLot;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.enums.SlotStatus;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.ParkingLotRepository;
import com.smartparking.repository.ParkingSlotRepository;
import com.smartparking.service.ParkingLotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingSlotRepository parkingSlotRepository;

    public ParkingLotServiceImpl(ParkingLotRepository parkingLotRepository,
                                  ParkingSlotRepository parkingSlotRepository) {
        this.parkingLotRepository = parkingLotRepository;
        this.parkingSlotRepository = parkingSlotRepository;
    }

    @Override
    @Transactional
    public ParkingLotResponse create(ParkingLotRequest request) {
        ParkingLot lot = new ParkingLot();
        lot.setName(request.getName());
        lot.setLocation(request.getLocation());
        lot.setTotalSlots(request.getTotalSlots());
        ParkingLot saved = parkingLotRepository.save(lot);

        // Auto-generate slots A01, A02, ... up to totalSlots so the lot is immediately usable.
        for (int i = 1; i <= request.getTotalSlots(); i++) {
            ParkingSlot slot = new ParkingSlot();
            slot.setSlotNumber(String.format("A%02d", i));
            slot.setStatus(SlotStatus.AVAILABLE);
            slot.setParkingLot(saved);
            parkingSlotRepository.save(slot);
        }

        return toResponse(saved);
    }

    @Override
    public List<ParkingLotResponse> getAll() {
        return parkingLotRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ParkingLotResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    @Override
    public ParkingLotResponse update(Long id, ParkingLotRequest request) {
        ParkingLot lot = findEntity(id);
        lot.setName(request.getName());
        lot.setLocation(request.getLocation());
        lot.setTotalSlots(request.getTotalSlots());
        return toResponse(parkingLotRepository.save(lot));
    }

    @Override
    public void delete(Long id) {
        parkingLotRepository.delete(findEntity(id));
    }

    @Override
    public List<ParkingSlotResponse> getSlotsForLot(Long lotId) {
        return parkingSlotRepository.findByParkingLotId(lotId).stream()
                .map(s -> new ParkingSlotResponse(s.getId(), s.getSlotNumber(), s.getStatus(), lotId))
                .toList();
    }

    private ParkingLot findEntity(Long id) {
        return parkingLotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking lot not found with id " + id));
    }

    private ParkingLotResponse toResponse(ParkingLot lot) {
        return new ParkingLotResponse(lot.getId(), lot.getName(), lot.getLocation(), lot.getTotalSlots());
    }
}
