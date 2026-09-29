package com.smartparking.controller;

import com.smartparking.dto.PurchaseOrderRequest;
import com.smartparking.dto.PurchaseOrderResponse;
import com.smartparking.dto.VendorBillResponse;
import com.smartparking.service.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderResponse> create(@Valid @RequestBody PurchaseOrderRequest request) {
        return new ResponseEntity<>(purchaseOrderService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderResponse>> getAll() {
        return ResponseEntity.ok(purchaseOrderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseOrderService.getById(id));
    }

    // Section 4A flow: Purchase Order -> Vendor Bill
    @PostMapping("/{id}/vendor-bill")
    public ResponseEntity<VendorBillResponse> convertToVendorBill(@PathVariable Long id) {
        return new ResponseEntity<>(purchaseOrderService.convertToVendorBill(id), HttpStatus.CREATED);
    }

    // Section 4A flow: Vendor Bill -> Payment -> Expense/Accounting Entry
    @PutMapping("/vendor-bill/{billId}/pay")
    public ResponseEntity<VendorBillResponse> payVendorBill(@PathVariable Long billId) {
        return ResponseEntity.ok(purchaseOrderService.payVendorBill(billId));
    }
}
