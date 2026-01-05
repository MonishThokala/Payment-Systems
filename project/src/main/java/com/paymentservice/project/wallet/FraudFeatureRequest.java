package com.paymentservice.project.wallet;

import java.math.BigDecimal;

public class FraudFeatureRequest {

	
	private BigDecimal amount;
    private long walletAgeDays;
    private int txnCountLast1Min;
    private BigDecimal avgTxnAmount;
    
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public long getWalletAgeDays() {
		return walletAgeDays;
	}
	public void setWalletAgeDays(long walletAgeDays) {
		this.walletAgeDays = walletAgeDays;
	}
	public int getTxnCountLast1Min() {
		return txnCountLast1Min;
	}
	public void setTxnCountLast1Min(int txnCountLast1Min) {
		this.txnCountLast1Min = txnCountLast1Min;
	}
	public BigDecimal getAvgTxnAmount() {
		return avgTxnAmount;
	}
	public void setAvgTxnAmount(BigDecimal avgTxnAmount) {
		this.avgTxnAmount = avgTxnAmount;
	}
     
}
