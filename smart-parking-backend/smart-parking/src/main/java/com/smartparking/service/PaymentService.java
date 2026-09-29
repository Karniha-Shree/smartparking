package com.smartparking.service;

import com.smartparking.dto.PaymentRequest;
import com.smartparking.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentResponse getById(Long id);
}
