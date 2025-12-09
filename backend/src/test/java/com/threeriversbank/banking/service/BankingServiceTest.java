package com.threeriversbank.banking.service;

import com.threeriversbank.banking.model.Account;
import com.threeriversbank.banking.model.Transaction;
import com.threeriversbank.banking.repository.AccountRepository;
import com.threeriversbank.banking.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankingServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @InjectMocks
    private BankingService bankingService;
    
    private Account fromAccount;
    private Account toAccount;
    
    @BeforeEach
    void setUp() {
        fromAccount = new Account("1001234567", "Checking", new BigDecimal("5000.00"), "John Doe");
        fromAccount.setId(1L);
        
        toAccount = new Account("2001234567", "Savings", new BigDecimal("10000.00"), "John Doe");
        toAccount.setId(2L);
    }
    
    @Test
    void testGetAccountByNumber_Success() {
        when(accountRepository.findByAccountNumber("1001234567")).thenReturn(Optional.of(fromAccount));
        
        Account result = bankingService.getAccountByNumber("1001234567");
        
        assertNotNull(result);
        assertEquals("1001234567", result.getAccountNumber());
        verify(accountRepository, times(1)).findByAccountNumber("1001234567");
    }
    
    @Test
    void testGetAccountByNumber_NotFound() {
        when(accountRepository.findByAccountNumber("9999999999")).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> bankingService.getAccountByNumber("9999999999"));
    }
    
    @Test
    void testTransferBetweenAccounts_Success() {
        BigDecimal transferAmount = new BigDecimal("1000.00");
        
        when(accountRepository.findByAccountNumber("1001234567")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("2001234567")).thenReturn(Optional.of(toAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Transaction result = bankingService.transferBetweenAccounts("1001234567", "2001234567", transferAmount, "Test transfer");
        
        assertNotNull(result);
        assertEquals(new BigDecimal("4000.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("11000.00"), toAccount.getBalance());
        assertEquals("TRANSFER", result.getType());
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    
    @Test
    void testTransferBetweenAccounts_InsufficientFunds() {
        BigDecimal transferAmount = new BigDecimal("10000.00");
        
        when(accountRepository.findByAccountNumber("1001234567")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("2001234567")).thenReturn(Optional.of(toAccount));
        
        assertThrows(RuntimeException.class, () -> 
            bankingService.transferBetweenAccounts("1001234567", "2001234567", transferAmount, "Test transfer"));
    }
    
    @Test
    void testTransferBetweenAccounts_InvalidAmount() {
        BigDecimal transferAmount = new BigDecimal("-100.00");
        
        assertThrows(RuntimeException.class, () -> 
            bankingService.transferBetweenAccounts("1001234567", "2001234567", transferAmount, "Test transfer"));
    }
}
