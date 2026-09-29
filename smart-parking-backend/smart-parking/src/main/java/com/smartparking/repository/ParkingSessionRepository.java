package com.smartparking.repository;

import com.smartparking.entity.ParkingSession;
import com.smartparking.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {

    // Used at exit time and to prevent double entry for the same vehicle.
    Optional<ParkingSession> findByVehicleIdAndStatus(Long vehicleId, SessionStatus status);

    List<ParkingSession> findByVehicleId(Long vehicleId);

    List<ParkingSession> findByParkingSlotId(Long parkingSlotId);

    List<ParkingSession> findByStatus(SessionStatus status);
}
