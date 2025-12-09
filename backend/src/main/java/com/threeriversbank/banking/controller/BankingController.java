package com.threeriversbank.banking.controller;

import com.threeriversbank.banking.dto.TransferRequest;
import com.threeriversbank.banking.model.Account;
import com.threeriversbank.banking.model.Transaction;
import com.threeriversbank.banking.service.AccountService;
import com.threeriversbank.banking.service.TransactionService;
import com.threeriversbank.banking.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${cors.allowed.origins:http://localhost:8501}")
@Tag(name = "Banking", description = "Banking operations API for account management and transactions")
public class BankingController {
    
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final TransferService transferService;
    
    public BankingController(AccountService accountService, 
                           TransactionService transactionService,
                           TransferService transferService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.transferService = transferService;
    }
    
    @GetMapping("/accounts")
    @Operation(summary = "Get all accounts", description = "Retrieve a list of all bank accounts")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }
    
    @GetMapping("/accounts/{accountNumber}")
    @Operation(summary = "Get account by number", description = "Retrieve a specific account by its account number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved account"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<Account> getAccount(
            @Parameter(description = "Account number to retrieve", required = true)
            @PathVariable String accountNumber) {
        try {
            return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/transactions/{accountNumber}")
    @Operation(summary = "Get transactions", description = "Retrieve all transactions for a specific account")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved transactions")
    public ResponseEntity<List<Transaction>> getTransactions(
            @Parameter(description = "Account number to get transactions for", required = true)
            @PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountNumber(accountNumber));
    }
    
    @PostMapping("/transfer")
    @Operation(summary = "Transfer funds", description = "Transfer funds between two accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transfer successful"),
        @ApiResponse(responseCode = "400", description = "Invalid transfer request or insufficient funds")
    })
    public ResponseEntity<?> transfer(
            @Parameter(description = "Transfer request details", required = true)
            @RequestBody TransferRequest transferRequest) {
        try {
            String description = transferRequest.getDescription() != null ? 
                transferRequest.getDescription() : "Transfer";
            
            Transaction transaction = transferService.transferBetweenAccounts(
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
