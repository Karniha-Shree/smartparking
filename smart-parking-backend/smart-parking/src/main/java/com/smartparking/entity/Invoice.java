package com.smartparking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_session_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"vehicle", "parkingSlot"})
    private ParkingSession parkingSession;

    private BigDecimal amount;

    private LocalDateTime issueDate;

    // Convenience flag mirroring the linked Payment's status, kept simple for reporting.
    private boolean paid = false;
}
