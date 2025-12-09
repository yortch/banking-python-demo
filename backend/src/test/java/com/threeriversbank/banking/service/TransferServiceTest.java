package com.threeriversbank.banking.service;

import com.threeriversbank.banking.exception.AccountNotFoundException;
import com.threeriversbank.banking.exception.InsufficientFundsException;
import com.threeriversbank.banking.exception.InvalidTransferException;
import com.threeriversbank.banking.model.Account;
import com.threeriversbank.banking.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {
    
    @Mock
    private AccountService accountService;
    
    @Mock
    private TransactionService transactionService;
    
    @InjectMocks
    private TransferService transferService;
    
    private Account fromAccount;
    private Account toAccount;
    private Transaction mockTransaction;
    
    @BeforeEach
    void setUp() {
        fromAccount = new Account("1001234567", "Checking", new BigDecimal("5000.00"), "John Doe");
        fromAccount.setId(1L);
        
        toAccount = new Account("2001234567", "Savings", new BigDecimal("10000.00"), "John Doe");
        toAccount.setId(2L);
        
        mockTransaction = new Transaction(
            "1001234567",
            "2001234567",
            new BigDecimal("1000.00"),
            "TRANSFER",
            null,
            "Test transfer"
        );
        mockTransaction.setId(1L);
    }
    
    @Test
    void testTransferBetweenAccounts_Success() {
        BigDecimal transferAmount = new BigDecimal("1000.00");
        
        when(accountService.getAccountByNumber("1001234567")).thenReturn(fromAccount);
        when(accountService.getAccountByNumber("2001234567")).thenReturn(toAccount);
        when(accountService.updateAccountBalance(any(Account.class), any(BigDecimal.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionService.createTransaction(
            eq("1001234567"), 
            eq("2001234567"), 
            eq(transferAmount), 
            eq("TRANSFER"), 
            eq("Test transfer")
        )).thenReturn(mockTransaction);
        
        Transaction result = transferService.transferBetweenAccounts(
            "1001234567", 
            "2001234567", 
            transferAmount, 
            "Test transfer"
        );
        
        assertNotNull(result);
        assertEquals(mockTransaction.getId(), result.getId());
        
        // Verify account balance updates
        verify(accountService, times(1)).updateAccountBalance(fromAccount, new BigDecimal("4000.00"));
        verify(accountService, times(1)).updateAccountBalance(toAccount, new BigDecimal("11000.00"));
        
        // Verify transaction creation
        verify(transactionService, times(1)).createTransaction(
            "1001234567",
            "2001234567",
            transferAmount,
            "TRANSFER",
            "Test transfer"
        );
    }
    
    @Test
    void testTransferBetweenAccounts_InsufficientFunds() {
        BigDecimal transferAmount = new BigDecimal("10000.00");
        
        when(accountService.getAccountByNumber("1001234567")).thenReturn(fromAccount);
        when(accountService.getAccountByNumber("2001234567")).thenReturn(toAccount);
        
        InsufficientFundsException exception = assertThrows(
            InsufficientFundsException.class,
            () -> transferService.transferBetweenAccounts(
                "1001234567", 
                "2001234567", 
                transferAmount, 
                "Test transfer"
            )
        );
        
        assertEquals("Insufficient funds", exception.getMessage());
        
        // Verify no updates were made
        verify(accountService, never()).updateAccountBalance(any(), any());
        verify(transactionService, never()).createTransaction(any(), any(), any(), any(), any());
    }
    
    @Test
    void testTransferBetweenAccounts_InvalidAmount_Negative() {
        BigDecimal transferAmount = new BigDecimal("-100.00");
        
        InvalidTransferException exception = assertThrows(
            InvalidTransferException.class,
            () -> transferService.transferBetweenAccounts(
                "1001234567", 
                "2001234567", 
                transferAmount, 
                "Test transfer"
            )
        );
        
        assertEquals("Transfer amount must be positive", exception.getMessage());
        
        // Verify no account lookups or updates were made
        verify(accountService, never()).getAccountByNumber(any());
        verify(accountService, never()).updateAccountBalance(any(), any());
        verify(transactionService, never()).createTransaction(any(), any(), any(), any(), any());
    }
    
    @Test
    void testTransferBetweenAccounts_InvalidAmount_Zero() {
        BigDecimal transferAmount = BigDecimal.ZERO;
        
        InvalidTransferException exception = assertThrows(
            InvalidTransferException.class,
            () -> transferService.transferBetweenAccounts(
                "1001234567", 
                "2001234567", 
                transferAmount, 
                "Test transfer"
            )
        );
        
        assertEquals("Transfer amount must be positive", exception.getMessage());
        verify(accountService, never()).getAccountByNumber(any());
    }
    
    @Test
    void testTransferBetweenAccounts_FromAccountNotFound() {
        BigDecimal transferAmount = new BigDecimal("1000.00");
        
        when(accountService.getAccountByNumber("9999999999"))
            .thenThrow(new AccountNotFoundException("Account not found: 9999999999"));
        
        AccountNotFoundException exception = assertThrows(
            AccountNotFoundException.class,
            () -> transferService.transferBetweenAccounts(
                "9999999999", 
                "2001234567", 
                transferAmount, 
                "Test transfer"
            )
        );
        
        assertEquals("Account not found: 9999999999", exception.getMessage());
        verify(accountService, never()).updateAccountBalance(any(), any());
        verify(transactionService, never()).createTransaction(any(), any(), any(), any(), any());
    }
    
    @Test
    void testTransferBetweenAccounts_ToAccountNotFound() {
        BigDecimal transferAmount = new BigDecimal("1000.00");
        
        when(accountService.getAccountByNumber("1001234567")).thenReturn(fromAccount);
        when(accountService.getAccountByNumber("8888888888"))
            .thenThrow(new AccountNotFoundException("Account not found: 8888888888"));
        
        AccountNotFoundException exception = assertThrows(
            AccountNotFoundException.class,
            () -> transferService.transferBetweenAccounts(
                "1001234567", 
                "8888888888", 
                transferAmount, 
                "Test transfer"
            )
        );
        
        assertEquals("Account not found: 8888888888", exception.getMessage());
        verify(accountService, never()).updateAccountBalance(any(), any());
        verify(transactionService, never()).createTransaction(any(), any(), any(), any(), any());
    }
    
    @Test
    void testTransferBetweenAccounts_ExactBalance() {
        BigDecimal transferAmount = new BigDecimal("5000.00"); // Exact balance
        
        when(accountService.getAccountByNumber("1001234567")).thenReturn(fromAccount);
        when(accountService.getAccountByNumber("2001234567")).thenReturn(toAccount);
        when(accountService.updateAccountBalance(any(Account.class), any(BigDecimal.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionService.createTransaction(any(), any(), any(), any(), any()))
            .thenReturn(mockTransaction);
        
        Transaction result = transferService.transferBetweenAccounts(
            "1001234567", 
            "2001234567", 
            transferAmount, 
            "Test transfer"
        );
        
        assertNotNull(result);
        verify(accountService, times(1)).updateAccountBalance(fromAccount, new BigDecimal("0.00"));
        verify(accountService, times(1)).updateAccountBalance(toAccount, new BigDecimal("15000.00"));
    }
}
