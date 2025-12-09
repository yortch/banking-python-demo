package com.threeriversbank.banking.service;

import com.threeriversbank.banking.model.Transaction;
import com.threeriversbank.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing transaction-related operations.
 * Handles transaction recording and retrieval.
 */
@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    
    /**
     * Retrieves all transactions for a specific account.
     * Returns transactions where the account is either the sender or receiver.
     * 
     * @param accountNumber The account number to get transactions for
     * @return List of transactions ordered by timestamp (most recent first)
     */
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findByFromAccountNumberOrToAccountNumberOrderByTimestampDesc(
                accountNumber, accountNumber);
    }
    
    /**
     * Creates and records a new transaction.
     * 
     * @param fromAccountNumber The account number funds are transferred from
     * @param toAccountNumber The account number funds are transferred to
     * @param amount The amount being transferred
     * @param type The type of transaction (e.g., "TRANSFER")
     * @param description Optional description of the transaction
     * @return The created transaction
     */
    public Transaction createTransaction(String fromAccountNumber, String toAccountNumber, 
                                        BigDecimal amount, String type, String description) {
        Transaction transaction = new Transaction(
                fromAccountNumber,
                toAccountNumber,
                amount,
                type,
                LocalDateTime.now(),
                description
        );
        return transactionRepository.save(transaction);
    }
}
