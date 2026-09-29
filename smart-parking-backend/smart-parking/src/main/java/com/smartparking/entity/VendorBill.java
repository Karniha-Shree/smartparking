package com.smartparking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smartparking.enums.VendorBillStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendor_bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String billNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false, unique = true)
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    private BigDecimal amount;

    private LocalDateTime billDate;

    @Enumerated(EnumType.STRING)
    private VendorBillStatus status = VendorBillStatus.PENDING;
}
