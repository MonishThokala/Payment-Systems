package com.paymentservice.project.wallet;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "wallet_transaction",
    uniqueConstraints = @UniqueConstraint(columnNames = "referenceId")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private TransactionType type;
    
    
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private BigDecimal amount;

    private String referenceId;

    private Instant createdAt;

    
    
	public WalletTransaction(Long id, Long userId, TransactionType type, BigDecimal amount, String referenceId,
			Instant createdAt, TransactionStatus status) {
		super();
		this.id = id;
		this.userId = userId;
		this.type = type;
		this.amount = amount;
		this.referenceId = referenceId;
		this.createdAt = createdAt;
		this.status = status;
	}
	
	
	public WalletTransaction() {
		super();
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
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

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
    
}

