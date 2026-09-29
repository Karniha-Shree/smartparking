package com.smartparking.service;

import com.smartparking.dto.PurchaseOrderRequest;
import com.smartparking.dto.PurchaseOrderResponse;
import com.smartparking.dto.VendorBillResponse;

import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrderResponse create(PurchaseOrderRequest request);
    List<PurchaseOrderResponse> getAll();
    PurchaseOrderResponse getById(Long id);

    // Converts an existing purchase order into a VendorBill (section 4A flow).
    VendorBillResponse convertToVendorBill(Long purchaseOrderId);

    // Pays a vendor bill -> records the maintenance/expense journal entry.
    VendorBillResponse payVendorBill(Long vendorBillId);
}
