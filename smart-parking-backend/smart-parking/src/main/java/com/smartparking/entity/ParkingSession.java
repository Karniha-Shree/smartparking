package com.smartparking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smartparking.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_slot_id", nullable = false)
    @JsonIgnoreProperties("parkingLot")
    private ParkingSlot parkingSlot;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    // Duration in minutes, filled in at exit time.
    private Long durationMinutes;

    // Final calculated fee, filled in at exit time.
    private BigDecimal fee;

    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.ACTIVE;
}
