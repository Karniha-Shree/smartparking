package com.smartparking.service.impl;

import com.smartparking.dto.ParkingEntryRequest;
import com.smartparking.dto.ParkingExitResponse;
import com.smartparking.dto.ParkingSlotResponse;
import com.smartparking.entity.*;
import com.smartparking.enums.PaymentStatus;
import com.smartparking.enums.SessionStatus;
import com.smartparking.enums.SlotStatus;
import com.smartparking.exception.InvalidParkingSessionException;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.exception.SlotNotAvailableException;
import com.smartparking.repository.*;
import com.smartparking.service.InvoiceService;
import com.smartparking.service.ParkingService;
import com.smartparking.service.PricingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingServiceImpl implements ParkingService {

    private final VehicleRepository vehicleRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingSessionRepository parkingSessionRepository;
    private final PricingService pricingService;
    private final InvoiceService invoiceService;

    public ParkingServiceImpl(VehicleRepository vehicleRepository,
                               ParkingLotRepository parkingLotRepository,
                               ParkingSlotRepository parkingSlotRepository,
                               ParkingSessionRepository parkingSessionRepository,
                               PricingService pricingService,
                               InvoiceService invoiceService) {
        this.vehicleRepository = vehicleRepository;
        this.parkingLotRepository = parkingLotRepository;
        this.parkingSlotRepository = parkingSlotRepository;
        this.parkingSessionRepository = parkingSessionRepository;
        this.pricingService = pricingService;
        this.invoiceService = invoiceService;
    }

    @Override
    @Transactional
    public ParkingSession vehicleEntry(ParkingEntryRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id " + request.getVehicleId()));

        // A vehicle can't enter twice while it already has an active session.
        parkingSessionRepository.findByVehicleIdAndStatus(vehicle.getId(), SessionStatus.ACTIVE)
                .ifPresent(s -> {
                    throw new InvalidParkingSessionException(
                            "Vehicle " + vehicle.getVehicleNumber() + " already has an active parking session (id "
                                    + s.getId() + ")");
                });

        if (!parkingLotRepository.existsById(request.getParkingLotId())) {
            throw new ResourceNotFoundException("Parking lot not found with id " + request.getParkingLotId());
        }

        ParkingSlot slot = parkingSlotRepository
                .findFirstByParkingLotIdAndStatus(request.getParkingLotId(), SlotStatus.AVAILABLE)
                .orElseThrow(() -> new SlotNotAvailableException("No parking slot available in lot "
                        + request.getParkingLotId()));

        slot.setStatus(SlotStatus.OCCUPIED);
        parkingSlotRepository.save(slot);

        ParkingSession session = new ParkingSession();
        session.setVehicle(vehicle);
        session.setParkingSlot(slot);
        session.setEntryTime(LocalDateTime.now());
        session.setStatus(SessionStatus.ACTIVE);

        return parkingSessionRepository.save(session);
    }

    @Override
    @Transactional
    public ParkingExitResponse vehicleExit(Long sessionId) {
        ParkingSession session = parkingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Parking session not found with id " + sessionId));

        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new InvalidParkingSessionException("Parking session " + sessionId + " is not active");
        }

        LocalDateTime exitTime = LocalDateTime.now();
        long durationMinutes = Duration.between(session.getEntryTime(), exitTime).toMinutes();

        BigDecimal fee = pricingService.calculateFee(durationMinutes, exitTime);

        session.setExitTime(exitTime);
        session.setDurationMinutes(durationMinutes);
        session.setFee(fee);
        session.setStatus(SessionStatus.COMPLETED);
        parkingSessionRepository.save(session);

        // Release the slot.
        ParkingSlot slot = session.getParkingSlot();
        slot.setStatus(SlotStatus.AVAILABLE);
        parkingSlotRepository.save(slot);

        // Generate invoice for this session.
        Invoice invoice = invoiceService.generateInvoice(session, fee);

        return new ParkingExitResponse(
                session.getId(),
                session.getVehicle().getVehicleNumber(),
                slot.getSlotNumber(),
                session.getEntryTime(),
                session.getExitTime(),
                durationMinutes,
                fee,
                invoice.getInvoiceNumber(),
                PaymentStatus.PENDING
        );
    }

    @Override
    public ParkingSession getSession(Long sessionId) {
        return parkingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Parking session not found with id " + sessionId));
    }

    @Override
    public List<ParkingSlotResponse> getAvailableSlots() {
        return parkingSlotRepository.findByStatus(SlotStatus.AVAILABLE).stream()
                .map(s -> new ParkingSlotResponse(s.getId(), s.getSlotNumber(), s.getStatus(),
                        s.getParkingLot().getId()))
                .toList();
    }
}
