package com.paymentservice.project.wallet;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

@Service
public class FraudDetectionService {
	
	public FraudDetection evaluate(CreditDebitRequest request, Wallet wallet) {

        int riskScore = 0;

        // Rule 1: Large amount
        if (request.getAmount().compareTo(BigDecimal.valueOf(100000)) > 0) {
            riskScore += 50;
        }

        // Rule 2: Too many transactions in short time
        if (wallet.getLastTransaction() != null &&
            Duration.between(wallet.getLastTransaction(), Instant.now()).toSeconds() < 10) {
            riskScore += 30;
        }

        // Rule 3: New wallet
        if (wallet.getCreatedTime()
                .isAfter(Instant.now().minus(Duration.ofDays(1)))) {
            riskScore += 20;
        }

        // Decision
        if (riskScore >= 70) return FraudDetection.BLOCK;
        if (riskScore >= 40) return FraudDetection.REVIEW;

        return FraudDetection.ALLOW;
    }
}
