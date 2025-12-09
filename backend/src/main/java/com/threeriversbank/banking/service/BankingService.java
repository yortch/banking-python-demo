package com.threeriversbank.banking.service;

import com.threeriversbank.banking.exception.AccountNotFoundException;
import com.threeriversbank.banking.exception.InsufficientFundsException;
import com.threeriversbank.banking.exception.InvalidTransferException;
import com.threeriversbank.banking.model.Account;
import com.threeriversbank.banking.model.Transaction;
import com.threeriversbank.banking.repository.AccountRepository;
import com.threeriversbank.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankingService {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    
    public BankingService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }
    
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }
    
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findByFromAccountNumberOrToAccountNumberOrderByTimestampDesc(accountNumber, accountNumber);
    }
    
    @Transactional
    public Transaction transferBetweenAccounts(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException("Transfer amount must be positive");
        }
        
        Account fromAccount = getAccountByNumber(fromAccountNumber);
        Account toAccount = getAccountByNumber(toAccountNumber);
        
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        
        Transaction transaction = new Transaction(
                fromAccountNumber,
                toAccountNumber,
                amount,
                "TRANSFER",
                LocalDateTime.now(),
                description
        );
        
        return transactionRepository.save(transaction);
    }
}
