package com.paymentservice.project.wallet;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "wallet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
	

	public Wallet(Long userId, Long version, BigDecimal balance,String currency) {
		super();
		this.userId = userId;
		this.version = version;
		this.balance = balance;
		this.currency = currency;
	}
	
	public Wallet() {
    }

	@Id
    private Long userId;

    @Version
    private Long version;

    @Column(nullable = false)
    private BigDecimal balance;
    
    private String currency;
    
    private Instant lastTransaction;
    
    private Instant createdTime;

	public Instant getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Instant createdTime) {
		this.createdTime = createdTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
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

	public Instant getLastTransaction() {
		return lastTransaction;
	}

	public void setLastTransaction(Instant lastTransaction) {
		this.lastTransaction = lastTransaction;
	}

}
