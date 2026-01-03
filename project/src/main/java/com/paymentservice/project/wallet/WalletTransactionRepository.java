package com.paymentservice.project.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

boolean existsByReferenceId(String referenceId);
}
