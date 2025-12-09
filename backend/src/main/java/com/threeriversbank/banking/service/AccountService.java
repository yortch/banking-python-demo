package com.threeriversbank.banking.service;

import com.threeriversbank.banking.exception.AccountNotFoundException;
import com.threeriversbank.banking.model.Account;
import com.threeriversbank.banking.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service for managing account-related operations.
 * Handles account retrieval and balance updates.
 */
@Service
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    /**
     * Retrieves all accounts in the system.
     * 
     * @return List of all accounts
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    /**
     * Retrieves a specific account by its account number.
     * 
     * @param accountNumber The account number to search for
     * @return The account with the given account number
     * @throws AccountNotFoundException if the account is not found
     */
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }
    
    /**
     * Updates the balance of an account.
     * 
     * @param account The account to update
     * @param newBalance The new balance to set
     * @return The updated account
     */
    public Account updateAccountBalance(Account account, BigDecimal newBalance) {
        account.setBalance(newBalance);
        return accountRepository.save(account);
    }
}
