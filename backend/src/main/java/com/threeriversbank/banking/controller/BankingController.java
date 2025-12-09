package com.threeriversbank.banking.controller;

import com.threeriversbank.banking.dto.TransferRequest;
import com.threeriversbank.banking.model.Account;
import com.threeriversbank.banking.model.Transaction;
import com.threeriversbank.banking.service.BankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${cors.allowed.origins:http://localhost:8501}")
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
    public ResponseEntity<?> transfer(@RequestBody TransferRequest transferRequest) {
        try {
            String description = transferRequest.getDescription() != null ? 
                transferRequest.getDescription() : "Transfer";
            
            Transaction transaction = bankingService.transferBetweenAccounts(
                transferRequest.getFromAccount(), 
                transferRequest.getToAccount(), 
                transferRequest.getAmount(), 
                description
            );
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
