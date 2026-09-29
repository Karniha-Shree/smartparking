package com.smartparking.service.impl;

import com.smartparking.dto.PurchaseOrderRequest;
import com.smartparking.dto.PurchaseOrderResponse;
import com.smartparking.dto.VendorBillResponse;
import com.smartparking.entity.PurchaseOrder;
import com.smartparking.entity.Vendor;
import com.smartparking.entity.VendorBill;
import com.smartparking.enums.PurchaseOrderStatus;
import com.smartparking.enums.VendorBillStatus;
import com.smartparking.exception.PaymentException;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.PurchaseOrderRepository;
import com.smartparking.repository.VendorBillRepository;
import com.smartparking.repository.VendorRepository;
import com.smartparking.service.AccountingService;
import com.smartparking.service.PurchaseOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final VendorRepository vendorRepository;
    private final VendorBillRepository vendorBillRepository;
    private final AccountingService accountingService;

    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository,
                                     VendorRepository vendorRepository,
                                     VendorBillRepository vendorBillRepository,
                                     AccountingService accountingService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.vendorRepository = vendorRepository;
        this.vendorBillRepository = vendorBillRepository;
        this.accountingService = accountingService;
    }

    @Override
    public PurchaseOrderResponse create(PurchaseOrderRequest request) {
        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with id " + request.getVendorId()));

        PurchaseOrder po = new PurchaseOrder();
        po.setPoNumber("PO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        po.setVendor(vendor);
        po.setDescription(request.getDescription());
        po.setAmount(request.getAmount());
        po.setStatus(PurchaseOrderStatus.CREATED);
        po.setOrderDate(LocalDateTime.now());

        return toResponse(purchaseOrderRepository.save(po));
    }

    @Override
    public List<PurchaseOrderResponse> getAll() {
        return purchaseOrderRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public PurchaseOrderResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    @Override
    @Transactional
    public VendorBillResponse convertToVendorBill(Long purchaseOrderId) {
        PurchaseOrder po = findEntity(purchaseOrderId);

        if (po.getStatus() == PurchaseOrderStatus.BILLED || po.getStatus() == PurchaseOrderStatus.CLOSED) {
            throw new PaymentException("Purchase order " + po.getPoNumber() + " has already been billed");
        }

        VendorBill bill = new VendorBill();
        bill.setBillNumber("BILL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        bill.setPurchaseOrder(po);
        bill.setVendor(po.getVendor());
        bill.setAmount(po.getAmount());
        bill.setBillDate(LocalDateTime.now());
        bill.setStatus(VendorBillStatus.PENDING);
        VendorBill saved = vendorBillRepository.save(bill);

        po.setStatus(PurchaseOrderStatus.BILLED);
        purchaseOrderRepository.save(po);

        // Debit Maintenance Expense, Credit Contractor Creditors / Vendor Payables (spec section 3C/4A).
        accountingService.recordJournalEntry(
                "Maintenance Expense",
                "Contractor Creditors / Vendor Payables",
                saved.getAmount(),
                "Vendor bill " + saved.getBillNumber() + " for PO " + po.getPoNumber(),
                "VENDOR_BILL",
                saved.getId()
        );

        return toBillResponse(saved);
    }

    @Override
    @Transactional
    public VendorBillResponse payVendorBill(Long vendorBillId) {
        VendorBill bill = vendorBillRepository.findById(vendorBillId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor bill not found with id " + vendorBillId));

        if (bill.getStatus() == VendorBillStatus.PAID) {
            throw new PaymentException("Vendor bill " + bill.getBillNumber() + " is already paid");
        }

        bill.setStatus(VendorBillStatus.PAID);
        VendorBill saved = vendorBillRepository.save(bill);

        bill.getPurchaseOrder().setStatus(PurchaseOrderStatus.CLOSED);
        purchaseOrderRepository.save(bill.getPurchaseOrder());

        // Debit Contractor Creditors / Vendor Payables (clearing the liability), Credit Cash.
        accountingService.recordJournalEntry(
                "Contractor Creditors / Vendor Payables",
                "Cash",
                saved.getAmount(),
                "Payment of vendor bill " + saved.getBillNumber(),
                "VENDOR_BILL_PAYMENT",
                saved.getId()
        );

        return toBillResponse(saved);
    }

    private PurchaseOrder findEntity(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found with id " + id));
    }

    private PurchaseOrderResponse toResponse(PurchaseOrder po) {
        return new PurchaseOrderResponse(po.getId(), po.getPoNumber(), po.getVendor().getId(),
                po.getVendor().getName(), po.getDescription(), po.getAmount(), po.getStatus(), po.getOrderDate());
    }

    private VendorBillResponse toBillResponse(VendorBill b) {
        return new VendorBillResponse(b.getId(), b.getBillNumber(), b.getPurchaseOrder().getId(),
                b.getVendor().getId(), b.getVendor().getName(), b.getAmount(), b.getBillDate(), b.getStatus());
    }
}
