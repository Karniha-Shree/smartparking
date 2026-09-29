package com.smartparking.service.impl;

import com.smartparking.entity.Account;
import com.smartparking.entity.JournalEntry;
import com.smartparking.enums.AccountType;
import com.smartparking.repository.AccountRepository;
import com.smartparking.repository.JournalEntryRepository;
import com.smartparking.service.AccountingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountingServiceImpl implements AccountingService {

    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;

    public AccountingServiceImpl(AccountRepository accountRepository,
                                  JournalEntryRepository journalEntryRepository) {
        this.accountRepository = accountRepository;
        this.journalEntryRepository = journalEntryRepository;
    }

    @Override
    @Transactional
    public void recordJournalEntry(String debitAccountName, String creditAccountName,
                                    BigDecimal amount, String description,
                                    String referenceType, Long referenceId) {

        Account debitAccount = getOrCreateAccount(debitAccountName, guessType(debitAccountName));
        Account creditAccount = getOrCreateAccount(creditAccountName, guessType(creditAccountName));

        JournalEntry entry = new JournalEntry();
        entry.setJournalDate(LocalDateTime.now());
        entry.setDescription(description);
        entry.setDebitAccount(debitAccount);
        entry.setCreditAccount(creditAccount);
        entry.setAmount(amount);
        entry.setReferenceType(referenceType);
        entry.setReferenceId(referenceId);
        journalEntryRepository.save(entry);

        // Simple double-entry balance update:
        // Debiting an ASSET/EXPENSE account increases it; crediting an INCOME/LIABILITY account increases it.
        debitAccount.setBalance(debitAccount.getBalance().add(amount));
        creditAccount.setBalance(creditAccount.getBalance().add(amount));
        accountRepository.save(debitAccount);
        accountRepository.save(creditAccount);
    }

    @Override
    public Account getOrCreateAccount(String accountName, AccountType type) {
        return accountRepository.findByAccountName(accountName)
                .orElseGet(() -> accountRepository.save(new Account(null, accountName, type, BigDecimal.ZERO)));
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Small helper so callers can just pass account names without worrying about
    // classification the first time an account is auto-created.
    private AccountType guessType(String accountName) {
        String lower = accountName.toLowerCase();
        if (lower.contains("revenue") || lower.contains("income")) return AccountType.INCOME;
        if (lower.contains("expense")) return AccountType.EXPENSE;
        if (lower.contains("payable") || lower.contains("creditor")) return AccountType.LIABILITY;
        return AccountType.ASSET;
    }
}
