package com.paymentservice.project.wallet;

import lombok.*;
import java.math.*;
import java.time.Instant;

@Getter
@AllArgsConstructor
public class WalletResponse {
	
	private Long userId;
    private BigDecimal balance;
    private String currency;
    private Instant createdTime;
    private Instant lastTransactionTime;
    

    
	public WalletResponse(Long userId, BigDecimal balance, String currency, Instant createdTime,
			Instant lastTransactionTime) {
		super();
		this.userId = userId;
		this.balance = balance;
		this.currency = currency;
		this.createdTime = createdTime;
		this.lastTransactionTime = lastTransactionTime;
	}

	public WalletResponse()
	{
		
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Instant getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Instant createdTime) {
		this.createdTime = createdTime;
	}

	public Instant getLastTransactionTime() {
		return lastTransactionTime;
	}

	public void setLastTransactionTime(Instant lastTransactionTime) {
		this.lastTransactionTime = lastTransactionTime;
	}
	
}
