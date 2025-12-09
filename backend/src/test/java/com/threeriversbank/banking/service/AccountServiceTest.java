package com.threeriversbank.banking.service;

import com.threeriversbank.banking.exception.AccountNotFoundException;
import com.threeriversbank.banking.model.Account;
import com.threeriversbank.banking.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @InjectMocks
    private AccountService accountService;
    
    private Account account1;
    private Account account2;
    
    @BeforeEach
    void setUp() {
        account1 = new Account("1001234567", "Checking", new BigDecimal("5000.00"), "John Doe");
        account1.setId(1L);
        
        account2 = new Account("2001234567", "Savings", new BigDecimal("10000.00"), "John Doe");
        account2.setId(2L);
    }
    
    @Test
    void testGetAllAccounts_Success() {
        List<Account> accounts = Arrays.asList(account1, account2);
        when(accountRepository.findAll()).thenReturn(accounts);
        
        List<Account> result = accountService.getAllAccounts();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1001234567", result.get(0).getAccountNumber());
        assertEquals("2001234567", result.get(1).getAccountNumber());
        verify(accountRepository, times(1)).findAll();
    }
    
    @Test
    void testGetAccountByNumber_Success() {
        when(accountRepository.findByAccountNumber("1001234567")).thenReturn(Optional.of(account1));
        
        Account result = accountService.getAccountByNumber("1001234567");
        
        assertNotNull(result);
        assertEquals("1001234567", result.getAccountNumber());
        assertEquals("Checking", result.getAccountType());
        assertEquals(new BigDecimal("5000.00"), result.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber("1001234567");
    }
    
    @Test
    void testGetAccountByNumber_NotFound() {
        when(accountRepository.findByAccountNumber("9999999999")).thenReturn(Optional.empty());
        
        AccountNotFoundException exception = assertThrows(
            AccountNotFoundException.class,
            () -> accountService.getAccountByNumber("9999999999")
        );
        
        assertEquals("Account not found: 9999999999", exception.getMessage());
        verify(accountRepository, times(1)).findByAccountNumber("9999999999");
    }
    
    @Test
    void testUpdateAccountBalance_Success() {
        BigDecimal newBalance = new BigDecimal("3000.00");
        when(accountRepository.save(any(Account.class))).thenReturn(account1);
        
        Account result = accountService.updateAccountBalance(account1, newBalance);
        
        assertNotNull(result);
        assertEquals(newBalance, result.getBalance());
        verify(accountRepository, times(1)).save(account1);
    }
    
    @Test
    void testUpdateAccountBalance_VerifyBalanceSet() {
        BigDecimal newBalance = new BigDecimal("7500.50");
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account acc = invocation.getArgument(0);
            assertEquals(newBalance, acc.getBalance());
            return acc;
        });
        
        accountService.updateAccountBalance(account1, newBalance);
        
        verify(accountRepository, times(1)).save(account1);
    }
}
