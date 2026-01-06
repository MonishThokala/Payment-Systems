package com.paymentservice.project.wallet;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class ShadowFraudLog {
    @Id @GeneratedValue
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private double fraudScore;
    private Instant createdAt = Instant.now();
}
