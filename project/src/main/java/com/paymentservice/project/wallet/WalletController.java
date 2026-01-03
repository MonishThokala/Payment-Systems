package com.paymentservice.project.wallet;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/create")
    public String create(@RequestBody CreateWalletRequest request) {
        walletService.createWallet(request.getUserId(),request.getCurrency());
        return "Created With given request";
    }

    @PostMapping("/credit")
    public ResponseEntity<WalletResponse> credit(@RequestBody CreditDebitRequest request) {
        WalletResponse response = walletService.credit(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/debit")
    public ResponseEntity<?> debit(@RequestBody CreditDebitRequest request) {
        try {
            WalletResponse response = walletService.debit(request);
            return ResponseEntity.ok(response);
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponse> get(@PathVariable Long userId) {
        WalletResponse response = walletService.getWallet(userId);
        if (response == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public String home() {
        return "Payment Systems API is running!";
    }
    
    @PutMapping("/update")
    public String update(@RequestBody CreateWalletRequest request)
    {
    	walletService.updateWallet(request.getUserId(),request.getCurrency(),request.getBalance());
    	return "Updated with Given Details";
    }
}
