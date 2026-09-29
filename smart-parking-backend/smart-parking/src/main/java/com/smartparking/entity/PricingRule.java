package com.smartparking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

// Configurable dynamic pricing. Example:
// Normal rule: rate=50, peakHour=false, active=true, no start/end time restriction
// Peak rule:   rate=80, peakHour=true, startTime=17:00, endTime=21:00, active=true
@Entity
@Table(name = "pricing_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Rate per hour.
    @Column(nullable = false)
    private BigDecimal rate;

    // If startTime/endTime are both null, the rule is treated as an "all day" rule.
    private LocalTime startTime;

    private LocalTime endTime;

    private boolean peakHour = false;

    private boolean active = true;
}
