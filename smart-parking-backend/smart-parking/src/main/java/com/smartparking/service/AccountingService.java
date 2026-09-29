package com.smartparking.service;

import com.smartparking.entity.Account;
import com.smartparking.enums.AccountType;

import java.math.BigDecimal;
import java.util.List;

public interface AccountingService {

    // Records one double-entry journal line: debits one account, credits another,
    // and updates both accounts' running balances.
    void recordJournalEntry(String debitAccountName, String creditAccountName,
                             BigDecimal amount, String description,
                             String referenceType, Long referenceId);

    Account getOrCreateAccount(String accountName, AccountType type);

    List<Account> getAllAccounts();
}
