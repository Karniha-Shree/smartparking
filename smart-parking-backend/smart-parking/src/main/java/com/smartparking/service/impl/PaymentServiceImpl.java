package com.smartparking.service.impl;

import com.smartparking.dto.PaymentRequest;
import com.smartparking.dto.PaymentResponse;
import com.smartparking.entity.Invoice;
import com.smartparking.entity.Payment;
import com.smartparking.enums.PaymentStatus;
import com.smartparking.exception.PaymentException;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.InvoiceRepository;
import com.smartparking.repository.PaymentRepository;
import com.smartparking.service.AccountingService;
import com.smartparking.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final AccountingService accountingService;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                               InvoiceRepository invoiceRepository,
                               AccountingService accountingService) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.accountingService = accountingService;
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id " + request.getInvoiceId()));

        if (invoice.isPaid()) {
            throw new PaymentException("Invoice " + invoice.getInvoiceNumber() + " is already paid");
        }

        if (request.getAmount().compareTo(invoice.getAmount()) != 0) {
            throw new PaymentException("Payment amount (" + request.getAmount()
                    + ") does not match invoice amount (" + invoice.getAmount() + ")");
        }

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setPaymentDate(LocalDateTime.now());
        Payment saved = paymentRepository.save(payment);

        invoice.setPaid(true);
        invoiceRepository.save(invoice);

        // Debit Cash, Credit Parking Tariff Revenue (section 3E / 15-PHASE6 of the spec).
        accountingService.recordJournalEntry(
                "Cash",
                "Parking Tariff Revenue",
                payment.getAmount(),
                "Parking payment for invoice " + invoice.getInvoiceNumber(),
                "PAYMENT",
                saved.getId()
        );

        return toResponse(saved);
    }

    @Override
    public PaymentResponse getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));
        return toResponse(payment);
    }

    private PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(p.getId(), p.getInvoice().getId(), p.getInvoice().getInvoiceNumber(),
                p.getAmount(), p.getPaymentMethod(), p.getPaymentStatus(), p.getPaymentDate());
    }
}
