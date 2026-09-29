package com.smartparking.repository;

import com.smartparking.entity.Account;
import com.smartparking.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountName(String accountName);
    List<Account> findByAccountType(AccountType accountType);
}
