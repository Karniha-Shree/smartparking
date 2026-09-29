package com.smartparking.service;

import com.smartparking.dto.BudgetRequest;
import com.smartparking.dto.BudgetResponse;

import java.util.List;

public interface BudgetService {
    BudgetResponse create(BudgetRequest request);
    List<BudgetResponse> getAll();
    BudgetResponse getById(Long id);
}
