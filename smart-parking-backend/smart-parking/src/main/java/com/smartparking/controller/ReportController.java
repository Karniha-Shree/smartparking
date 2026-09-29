package com.smartparking.controller;

import com.smartparking.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/balance-sheet")
    public ResponseEntity<Map<String, Object>> balanceSheet() {
        return ResponseEntity.ok(reportService.getBalanceSheet());
    }

    @GetMapping("/profit-loss")
    public ResponseEntity<Map<String, Object>> profitAndLoss() {
        return ResponseEntity.ok(reportService.getProfitAndLoss());
    }

    @GetMapping("/budget")
    public ResponseEntity<Map<String, Object>> budgetReport() {
        return ResponseEntity.ok(reportService.getBudgetReport());
    }

    @GetMapping("/parking-revenue")
    public ResponseEntity<Map<String, Object>> parkingRevenue() {
        return ResponseEntity.ok(reportService.getParkingRevenueReport());
    }
}
