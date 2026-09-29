package com.smartparking.controller;

import com.smartparking.dto.BudgetRequest;
import com.smartparking.dto.BudgetResponse;
import com.smartparking.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> create(@Valid @RequestBody BudgetRequest request) {
        return new ResponseEntity<>(budgetService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAll() {
        return ResponseEntity.ok(budgetService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(budgetService.getById(id));
    }
}
