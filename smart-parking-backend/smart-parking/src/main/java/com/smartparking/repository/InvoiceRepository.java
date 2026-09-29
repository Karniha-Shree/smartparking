package com.smartparking.repository;

import com.smartparking.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByParkingSessionId(Long parkingSessionId);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
