package com.smartparking.service;

import com.smartparking.dto.InvoiceResponse;
import com.smartparking.entity.Invoice;
import com.smartparking.entity.ParkingSession;

import java.math.BigDecimal;

public interface InvoiceService {
    Invoice generateInvoice(ParkingSession session, BigDecimal amount);
    InvoiceResponse getById(Long id);
}
