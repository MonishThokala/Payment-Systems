package com.paymentservice.project.wallet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(InsufficientBalanceException ex) {
    	ErrorResponse error = new ErrorResponse(
                "INSUFFICIENT_BALANCE",
                ex.getMessage()
            );
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DocumentVerificationException.class)
    public ResponseEntity<ErrorResponse> handleDocumentVerification(DocumentVerificationException ex) {
    	ErrorResponse error = new ErrorResponse(
                "DOCUMENT_NEEDED",
                ex.getMessage()
            );
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleFrauDetectionn(FraudDetectedException ex) {
    	ErrorResponse error = new ErrorResponse(
                "HIGH_RISK",
                ex.getMessage()
            );
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}

