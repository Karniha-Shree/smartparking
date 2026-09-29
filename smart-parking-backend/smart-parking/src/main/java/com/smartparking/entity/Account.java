package com.smartparking.entity;

import com.smartparking.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Chart of Accounts entry, e.g. "Cash", "Parking Tariff Revenue", "Maintenance Expense"
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountName;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private BigDecimal balance = BigDecimal.ZERO;
}
