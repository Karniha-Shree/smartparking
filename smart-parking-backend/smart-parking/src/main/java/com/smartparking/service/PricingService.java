package com.smartparking.service;

import com.smartparking.dto.PricingRuleRequest;
import com.smartparking.dto.PricingRuleResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PricingService {

    PricingRuleResponse create(PricingRuleRequest request);

    List<PricingRuleResponse> getAll();

    // Core pricing calculation used at vehicle exit.
    // durationMinutes: how long the vehicle was parked.
    // exitTime: used to decide which time-bound rule (e.g. peak hour) applies.
    BigDecimal calculateFee(long durationMinutes, LocalDateTime exitTime);
}
