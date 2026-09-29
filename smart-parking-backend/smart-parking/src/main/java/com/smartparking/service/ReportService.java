package com.smartparking.service;

import java.util.Map;

public interface ReportService {
    Map<String, Object> getBalanceSheet();
    Map<String, Object> getProfitAndLoss();
    Map<String, Object> getBudgetReport();
    Map<String, Object> getParkingRevenueReport();
}
