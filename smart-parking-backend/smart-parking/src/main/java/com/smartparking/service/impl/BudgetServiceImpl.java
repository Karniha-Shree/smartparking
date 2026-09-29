package com.smartparking.service.impl;

import com.smartparking.dto.*;
import com.smartparking.entity.Account;
import com.smartparking.entity.Budget;
import com.smartparking.entity.BudgetLine;
import com.smartparking.exception.ResourceNotFoundException;
import com.smartparking.repository.AccountRepository;
import com.smartparking.repository.BudgetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smartparking.service.BudgetService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final AccountRepository accountRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository, AccountRepository accountRepository) {
        this.budgetRepository = budgetRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public BudgetResponse create(BudgetRequest request) {
        Budget budget = new Budget();
        budget.setName(request.getName());
        budget.setStartDate(request.getStartDate());
        budget.setEndDate(request.getEndDate());

        if (request.getLines() != null) {
            for (BudgetLineRequest lineReq : request.getLines()) {
                Account account = accountRepository.findById(lineReq.getAccountId())
                        .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + lineReq.getAccountId()));

                BudgetLine line = new BudgetLine();
                line.setBudget(budget);
                line.setAccount(account);
                line.setPlannedAmount(lineReq.getPlannedAmount());
                // Actual amount reflects the account's current running balance at creation time,
                // giving an immediate planned-vs-actual comparison.
                line.setActualAmount(account.getBalance());
                budget.getLines().add(line);
            }
        }

        Budget saved = budgetRepository.save(budget);
        return toResponse(saved);
    }

    @Override
    public List<BudgetResponse> getAll() {
        return budgetRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public BudgetResponse getById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id " + id));
        return toResponse(budget);
    }

    private BudgetResponse toResponse(Budget b) {
        List<BudgetLineResponse> lineResponses = new ArrayList<>();
        for (BudgetLine line : b.getLines()) {
            BigDecimal variance = line.getActualAmount().subtract(line.getPlannedAmount());
            lineResponses.add(new BudgetLineResponse(line.getId(), line.getAccount().getId(),
                    line.getAccount().getAccountName(), line.getPlannedAmount(), line.getActualAmount(), variance));
        }
        return new BudgetResponse(b.getId(), b.getName(), b.getStartDate(), b.getEndDate(), lineResponses);
    }
}
