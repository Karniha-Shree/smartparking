package com.smartparking.dto;

import com.smartparking.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingExitResponse {
    private Long sessionId;
    private String vehicleNumber;
    private String slotNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private long durationMinutes;
    private BigDecimal parkingFee;
    private String invoiceNumber;
    private PaymentStatus paymentStatus;
}
