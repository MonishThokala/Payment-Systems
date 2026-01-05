package com.paymentservice.project.wallet;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

boolean existsByReferenceId(String referenceId);
//
//int countByWallet_IdAndCreatedAtAfter(Long userId, Instant after);
//
//@Query("SELECT AVG(t.amount) FROM WalletTransaction t WHERE t.walletId = :walletId")
//Optional<BigDecimal> findAverageAmountByWalletId(@Param("walletId") Long walletId);


@Query("""
        SELECT AVG(t.amount)
        FROM WalletTransaction t
        WHERE t.wallet.userId = :userId
    """)
    Optional<BigDecimal> findAverageAmountByUserId(
            @Param("userId") Long userId
    );

    int countByWallet_UserIdAndCreatedAtAfter(
            Long userId,
            Instant after
    );
}
