package com.threeriversbank.banking.controller;

import com.threeriversbank.banking.model.Account;
import com.threeriversbank.banking.model.Transaction;
import com.threeriversbank.banking.service.BankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BankingController {
    
    private final BankingService bankingService;
    
    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }
    
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(bankingService.getAllAccounts());
    }
    
    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        try {
            return ResponseEntity.ok(bankingService.getAccountByNumber(accountNumber));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/transactions/{accountNumber}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String accountNumber) {
        return ResponseEntity.ok(bankingService.getTransactionsByAccountNumber(accountNumber));
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody Map<String, String> transferRequest) {
        try {
            String fromAccount = transferRequest.get("fromAccount");
            String toAccount = transferRequest.get("toAccount");
            BigDecimal amount = new BigDecimal(transferRequest.get("amount"));
            String description = transferRequest.getOrDefault("description", "Transfer");
            
            Transaction transaction = bankingService.transferBetweenAccounts(fromAccount, toAccount, amount, description);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
