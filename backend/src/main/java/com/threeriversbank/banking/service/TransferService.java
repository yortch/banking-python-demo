package com.threeriversbank.banking.service;

import com.threeriversbank.banking.exception.InsufficientFundsException;
import com.threeriversbank.banking.exception.InvalidTransferException;
import com.threeriversbank.banking.model.Account;
import com.threeriversbank.banking.model.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service for orchestrating fund transfer operations.
 * Coordinates account balance updates and transaction recording.
 */
@Service
public class TransferService {
    
    private final AccountService accountService;
    private final TransactionService transactionService;
    
    public TransferService(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }
    
    /**
     * Transfers funds between two accounts.
     * This operation is transactional - both account updates and transaction recording
     * will succeed or fail together.
     * 
     * @param fromAccountNumber The account number to transfer from
     * @param toAccountNumber The account number to transfer to
     * @param amount The amount to transfer
     * @param description Optional description for the transfer
     * @return The created transaction record
     * @throws InvalidTransferException if the transfer amount is not positive
     * @throws InsufficientFundsException if the source account has insufficient funds
     * @throws com.threeriversbank.banking.exception.AccountNotFoundException if either account is not found
     */
    @Transactional
    public Transaction transferBetweenAccounts(String fromAccountNumber, String toAccountNumber, 
                                              BigDecimal amount, String description) {
        // Validate transfer amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException("Transfer amount must be positive");
        }
        
        // Retrieve both accounts
        Account fromAccount = accountService.getAccountByNumber(fromAccountNumber);
        Account toAccount = accountService.getAccountByNumber(toAccountNumber);
        
        // Validate sufficient funds
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        
        // Update account balances
        BigDecimal newFromBalance = fromAccount.getBalance().subtract(amount);
        BigDecimal newToBalance = toAccount.getBalance().add(amount);
        
        accountService.updateAccountBalance(fromAccount, newFromBalance);
        accountService.updateAccountBalance(toAccount, newToBalance);
        
        // Record the transaction
        return transactionService.createTransaction(
                fromAccountNumber, 
                toAccountNumber, 
                amount, 
                "TRANSFER", 
                description
        );
    }
}
