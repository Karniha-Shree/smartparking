package com.smartparking.controller;

import com.smartparking.dto.PricingRuleRequest;
import com.smartparking.dto.PricingRuleResponse;
import com.smartparking.service.PricingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Not in the original controller list, but needed so an Admin can actually configure
// dynamic pricing rules (see spec section 5) instead of them being fixed in the database.
@RestController
@RequestMapping("/api/pricing-rules")
public class PricingRuleController {

    private final PricingService pricingService;

    public PricingRuleController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping
    public ResponseEntity<PricingRuleResponse> create(@Valid @RequestBody PricingRuleRequest request) {
        return new ResponseEntity<>(pricingService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PricingRuleResponse>> getAll() {
        return ResponseEntity.ok(pricingService.getAll());
    }
}
