package com.paymentservice.project.wallet;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
public class CreateWalletRequest {
    private Long userId;
    private String currency;
    private BigDecimal balance;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	
    
}