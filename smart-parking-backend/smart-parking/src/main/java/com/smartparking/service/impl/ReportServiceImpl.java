package com.smartparking.service.impl;

import com.smartparking.entity.Account;
import com.smartparking.entity.Invoice;
import com.smartparking.enums.AccountType;
import com.smartparking.repository.AccountRepository;
import com.smartparking.repository.InvoiceRepository;
import com.smartparking.service.ReportService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private final AccountRepository accountRepository;
    private final InvoiceRepository invoiceRepository;

    public ReportServiceImpl(AccountRepository accountRepository, InvoiceRepository invoiceRepository) {
        this.accountRepository = accountRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Map<String, Object> getBalanceSheet() {
        List<Account> assets = accountRepository.findByAccountType(AccountType.ASSET);
        List<Account> liabilities = accountRepository.findByAccountType(AccountType.LIABILITY);

        BigDecimal totalAssets = sumBalances(assets);
        BigDecimal totalLiabilities = sumBalances(liabilities);

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("assets", assets);
        report.put("totalAssets", totalAssets);
        report.put("liabilities", liabilities);
        report.put("totalLiabilities", totalLiabilities);
        // Equity = Assets - Liabilities (simplified, since there is no separate Equity account type yet).
        report.put("equity", totalAssets.subtract(totalLiabilities));
        return report;
    }

    @Override
    public Map<String, Object> getProfitAndLoss() {
        List<Account> income = accountRepository.findByAccountType(AccountType.INCOME);
        List<Account> expenses = accountRepository.findByAccountType(AccountType.EXPENSE);

        BigDecimal totalIncome = sumBalances(income);
        BigDecimal totalExpenses = sumBalances(expenses);

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("income", income);
        report.put("totalIncome", totalIncome);
        report.put("expenses", expenses);
        report.put("totalExpenses", totalExpenses);
        report.put("netProfit", totalIncome.subtract(totalExpenses));
        return report;
    }

    @Override
    public Map<String, Object> getBudgetReport() {
        // Delegates the heavy lifting to BudgetService's own planned-vs-actual per line;
        // here we just summarize totals across all accounts for a quick admin snapshot.
        List<Account> allAccounts = accountRepository.findAll();
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("accountBalances", allAccounts);
        return report;
    }

    @Override
    public Map<String, Object> getParkingRevenueReport() {
        List<Invoice> invoices = invoiceRepository.findAll();

        BigDecimal totalInvoiced = invoices.stream()
                .map(Invoice::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCollected = invoices.stream()
                .filter(Invoice::isPaid)
                .map(Invoice::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalSessions = invoices.size();
        long paidInvoices = invoices.stream().filter(Invoice::isPaid).count();

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("totalSessions", totalSessions);
        report.put("totalInvoiced", totalInvoiced);
        report.put("totalCollected", totalCollected);
        report.put("outstanding", totalInvoiced.subtract(totalCollected));
        report.put("paidInvoices", paidInvoices);
        report.put("unpaidInvoices", totalSessions - paidInvoices);
        return report;
    }

    private BigDecimal sumBalances(List<Account> accounts) {
        return accounts.stream().map(Account::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
