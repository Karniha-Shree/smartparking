package com.smartparking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// A simple double-entry journal line: one debit account, one credit account, one amount.
@Entity
@Table(name = "journal_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime journalDate;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debit_account_id", nullable = false)
    @JsonIgnoreProperties("balance")
    private Account debitAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_account_id", nullable = false)
    @JsonIgnoreProperties("balance")
    private Account creditAccount;

    private BigDecimal amount;

    // What triggered this entry, e.g. "PAYMENT" or "VENDOR_BILL"
    private String referenceType;

    private Long referenceId;
}
