package com.smartparking.repository;

import com.smartparking.entity.BudgetLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetLineRepository extends JpaRepository<BudgetLine, Long> {
}
