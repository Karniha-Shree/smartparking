package com.smartparking.service.impl;

import com.smartparking.dto.InvoiceResponse;
import com.smartparking.entity.Invoice;
import com.smartparking.entity.ParkingSession;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.InvoiceRepository;
import com.smartparking.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Invoice generateInvoice(ParkingSession session, BigDecimal amount) {
        Invoice invoice = new Invoice();
        // Simple unique invoice number: INV- + random UUID chunk.
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setParkingSession(session);
        invoice.setAmount(amount);
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setPaid(false);
        return invoiceRepository.save(invoice);
    }

    @Override
    public InvoiceResponse getById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id " + id));
        return toResponse(invoice);
    }

    private InvoiceResponse toResponse(Invoice i) {
        return new InvoiceResponse(i.getId(), i.getInvoiceNumber(), i.getParkingSession().getId(),
                i.getAmount(), i.getIssueDate(), i.isPaid());
    }
}
