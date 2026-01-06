package com.paymentservice.project.wallet;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class WalletService {
	
	private static final Logger log =
	        LoggerFactory.getLogger(WalletService.class);


    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;
    private final FraudDetectionService fraudDetectionService;
    private final MlFraudClient mlFraudClient;
    private final MlFraudProperties fraudProperties;
    
    public WalletService(WalletRepository walletRepository,
            WalletTransactionRepository transactionRepository,FraudDetectionService fraudDetectionService, MlFraudClient mlFraudClient,
            MlFraudProperties fraudProperties) {
    	this.walletRepository = walletRepository;
    	this.transactionRepository = transactionRepository;
    	this.fraudDetectionService = fraudDetectionService;
    	this.mlFraudClient = mlFraudClient;
    	this.fraudProperties = fraudProperties;
    }
    
    private boolean isHighRisk(double fraudScore) {
        return fraudScore >= fraudProperties.getThreshold();
    }

    private FraudFeatureRequest buildFraudFeatures(
            Wallet wallet,
            CreditDebitRequest request) {

        FraudFeatureRequest features = new FraudFeatureRequest();

        features.setAmount(request.getAmount());

        features.setWalletAgeDays(
            Duration.between(wallet.getCreatedTime(), Instant.now()).toDays()
        );

        features.setTxnCountLast1Min(
            transactionRepository.countByWallet_UserIdAndCreatedAtAfter(
                wallet.getUserId(),
                Instant.now().minusSeconds(60)
            )
        );

        features.setAvgTxnAmount(
            transactionRepository.findAverageAmountByUserId(wallet.getUserId())
                .orElse(BigDecimal.ZERO));

        return features;
    }

    
    public String getWalletInfo(Long userId) {
        // Just a dummy example
        return "Wallet info for user " + userId;
    }
    
    @Transactional
    public void createWallet(Long userId,String currency) {
        Wallet wallet = new Wallet(userId, null, BigDecimal.ZERO,currency);
        walletRepository.save(wallet);
    }
    
    @Transactional
    public void updateWallet(Long userId,String currency,BigDecimal balance)
    {
    	Wallet wallet = walletRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    	
    	wallet.setCurrency(currency);
    	wallet.setCreatedTime(Instant.now());
    	wallet.setBalance(wallet.getBalance().add(balance));
    }

    @Transactional
    public WalletResponse credit(CreditDebitRequest request) {
    	
    	//Idempotency check
        if (transactionRepository.existsByReferenceId(request.getReferenceId())) {
            Wallet wallet = walletRepository.findById(request.getUserId()).orElseThrow();
            return new WalletResponse(wallet.getUserId(), wallet.getBalance(),wallet.getCurrency(),wallet.getCreatedTime(),wallet.getLastTransaction());
        }
        

        Wallet wallet = walletRepository.findById(request.getUserId()).orElseThrow();
        
        FraudFeatureRequest features = buildFraudFeatures(wallet, request);

        double fraudScore = mlFraudClient.getFraudScore(features);
        
        if (!fraudProperties.isShadowMode() && isHighRisk(fraudScore)) {
            throw new FraudDetectedException("Blocked by ML fraud detection");
        }

        if (fraudProperties.isShadowMode() && isHighRisk(fraudScore)) {
            log.warn("SHADOW MODE: High-risk txn user={}, score={}",
                     wallet.getUserId(), fraudScore);
        }

//        if (fraudScore >= 0.5) {
//            throw new FraudDetectedException("Blocked by ML fraud detection");
//        }
        
        
//        FraudDetection detection = fraudDetectionService.evaluate(request,wallet);
//        
//        if(detection==FraudDetection.BLOCK)
//        {
//        	throw new FraudDetectedException("Transaction is at high risk");
//        }
        
//        if (request.getAmount().compareTo(BigDecimal.valueOf(50000)) > 0) {
//        	throw new DocumentVerificationException("Verification of document is required to credit more than : " + request.getAmount());
//        }
        
        WalletTransaction txn = new WalletTransaction();
        
        txn.setId(null);
        txn.setUserId(request.getUserId());
        txn.setType(TransactionType.CREDIT);
        txn.setReferenceId(request.getReferenceId());
        txn.setCreatedAt(Instant.now());
        txn.setStatus(TransactionStatus.PENDING);
        
        try {
        	wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        	wallet.setLastTransaction(Instant.now());
        	txn.setStatus(TransactionStatus.SUCCESS);
        }
        
        catch(Exception e) {
        		txn.setStatus(TransactionStatus.FAILED);
        		throw e;
        }
        	
//        wallet.setBalance(wallet.getBalance().add(request.getAmount()));

//        WalletTransaction txn = new WalletTransaction(
//                null,
//                request.getUserId(),
//                TransactionType.CREDIT,
//                request.getAmount(),
//                request.getReferenceId(),
//                Instant.now(),
//                TransactionStatus.SUCCESS
//         );

        transactionRepository.save(txn);
        walletRepository.save(wallet);

        return new WalletResponse(wallet.getUserId(), wallet.getBalance(), wallet.getCurrency(), wallet.getCreatedTime(),wallet.getLastTransaction());
    }

    @Transactional
    public WalletResponse debit(CreditDebitRequest request) throws InsufficientBalanceException {

        if (transactionRepository.existsByReferenceId(request.getReferenceId())) {
            Wallet wallet = walletRepository.findById(request.getUserId()).orElseThrow();
            return new WalletResponse(wallet.getUserId(), wallet.getBalance(), wallet.getCurrency(),wallet.getCreatedTime(),wallet.getLastTransaction());
        }

        Wallet wallet = walletRepository.findById(request.getUserId()).orElseThrow();

        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance. Available: " + wallet.getBalance());
        }
        
 FraudDetection detection = fraudDetectionService.evaluate(request,wallet);
        
        if(detection==FraudDetection.BLOCK)
        {
        	throw new FraudDetectedException("Transaction is at high risk");
        }
        
        if (request.getAmount().compareTo(BigDecimal.valueOf(50000)) > 0) {
        	throw new DocumentVerificationException("Verification of document is required to credit more than : " + request.getAmount());
        }
        
        WalletTransaction txn = new WalletTransaction();
        
        txn.setId(null);
        txn.setUserId(request.getUserId());
        txn.setType(TransactionType.DEBIT);
        txn.setReferenceId(request.getReferenceId());
        txn.setCreatedAt(Instant.now());
        txn.setStatus(TransactionStatus.PENDING);
        
        try {
        	wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        	wallet.setLastTransaction(Instant.now());
        	txn.setStatus(TransactionStatus.SUCCESS);
        }
        
        catch(Exception e) {
        		txn.setStatus(TransactionStatus.FAILED);
        		throw e;
        }

//        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));

//        WalletTransaction txn = new WalletTransaction(
//                null,
//                request.getUserId(),
//                TransactionType.DEBIT,
//                request.getAmount(),
//                request.getReferenceId(),
//                Instant.now(),
//                TransactionStatus.SUCCESS
//        );

        transactionRepository.save(txn);
        walletRepository.save(wallet);

        return new WalletResponse(wallet.getUserId(), wallet.getBalance(),wallet.getCurrency(),wallet.getCreatedTime(),wallet.getLastTransaction());
    }

    public WalletResponse getWallet(Long userId) {
        Wallet wallet = walletRepository.findById(userId).orElseThrow();
        return new WalletResponse(wallet.getUserId(), wallet.getBalance(),wallet.getCurrency(),wallet.getCreatedTime(),wallet.getLastTransaction());
    }
}

