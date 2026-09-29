package com.smartparking.service.impl;

import com.smartparking.dto.PricingRuleRequest;
import com.smartparking.dto.PricingRuleResponse;
import com.smartparking.entity.PricingRule;
import com.smartparking.repository.PricingRuleRepository;
import com.smartparking.service.PricingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PricingServiceImpl implements PricingService {

    private final PricingRuleRepository pricingRuleRepository;

    public PricingServiceImpl(PricingRuleRepository pricingRuleRepository) {
        this.pricingRuleRepository = pricingRuleRepository;
    }

    @Override
    public PricingRuleResponse create(PricingRuleRequest request) {
        PricingRule rule = new PricingRule();
        rule.setName(request.getName());
        rule.setRate(request.getRate());
        rule.setStartTime(request.getStartTime());
        rule.setEndTime(request.getEndTime());
        rule.setPeakHour(request.isPeakHour());
        rule.setActive(request.isActive());

        PricingRule saved = pricingRuleRepository.save(rule);
        return toResponse(saved);
    }

    @Override
    public List<PricingRuleResponse> getAll() {
        return pricingRuleRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public BigDecimal calculateFee(long durationMinutes, LocalDateTime exitTime) {
        PricingRule rule = findApplicableRule(exitTime.toLocalTime());

        // Convert minutes to hours (rounded up: even a few extra minutes counts as a full hour,
        // which is standard practice for parking garages).
        BigDecimal hours = BigDecimal.valueOf(durationMinutes)
                .divide(BigDecimal.valueOf(60), 4, RoundingMode.CEILING)
                .setScale(0, RoundingMode.CEILING);

        // Minimum charge is 1 hour even for very short stays.
        if (hours.compareTo(BigDecimal.ONE) < 0) {
            hours = BigDecimal.ONE;
        }

        return rule.getRate().multiply(hours).setScale(2, RoundingMode.HALF_UP);
    }

    // Decides WHICH pricing rule applies at the moment of exit.
    // Priority: an active, time-bound peak rule whose window contains "now" wins.
    // Otherwise fall back to the first active "all day" (no start/end time) rule.
    // If nothing matches, fall back to a hard-coded default of 50/hour so the system never breaks.
    private PricingRule findApplicableRule(LocalTime now) {
        List<PricingRule> activeRules = pricingRuleRepository.findByActiveTrue();

        Optional<PricingRule> peakMatch = activeRules.stream()
                .filter(r -> r.getStartTime() != null && r.getEndTime() != null)
                .filter(r -> isWithinWindow(now, r.getStartTime(), r.getEndTime()))
                .max(Comparator.comparing(PricingRule::getRate));

        if (peakMatch.isPresent()) {
            return peakMatch.get();
        }

        Optional<PricingRule> allDayMatch = activeRules.stream()
                .filter(r -> r.getStartTime() == null && r.getEndTime() == null)
                .findFirst();

        if (allDayMatch.isPresent()) {
            return allDayMatch.get();
        }

        // Safety net default rule so PricingService never throws if admin hasn't configured rules yet.
        PricingRule fallback = new PricingRule();
        fallback.setName("Default");
        fallback.setRate(BigDecimal.valueOf(50));
        return fallback;
    }

    private boolean isWithinWindow(LocalTime now, LocalTime start, LocalTime end) {
        if (start.isBefore(end)) {
            return !now.isBefore(start) && !now.isAfter(end);
        }
        // Handles windows that cross midnight, e.g. 22:00 - 02:00.
        return !now.isBefore(start) || !now.isAfter(end);
    }

    private PricingRuleResponse toResponse(PricingRule r) {
        return new PricingRuleResponse(r.getId(), r.getName(), r.getRate(),
                r.getStartTime(), r.getEndTime(), r.isPeakHour(), r.isActive());
    }
}
