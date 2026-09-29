package com.smartparking.repository;

import com.smartparking.entity.VendorBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorBillRepository extends JpaRepository<VendorBill, Long> {
    Optional<VendorBill> findByPurchaseOrderId(Long purchaseOrderId);
}
