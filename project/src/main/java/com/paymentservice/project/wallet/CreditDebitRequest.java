package com.paymentservice.project.wallet;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
public class CreditDebitRequest {
    private Long userId;
    private BigDecimal amount;
    private String referenceId;
    
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

    
}
