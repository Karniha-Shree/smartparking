package com.smartparking.repository;

import com.smartparking.entity.ParkingSlot;
import com.smartparking.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    List<ParkingSlot> findByStatus(SlotStatus status);

    List<ParkingSlot> findByParkingLotId(Long parkingLotId);

    // First available slot in a given lot — used when a vehicle enters.
    Optional<ParkingSlot> findFirstByParkingLotIdAndStatus(Long parkingLotId, SlotStatus status);

    Optional<ParkingSlot> findBySlotNumber(String slotNumber);
}
