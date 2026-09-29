package com.smartparking.repository;

import com.smartparking.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByInvoiceId(Long invoiceId);
}
