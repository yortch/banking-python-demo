package com.threeriversbank.banking.service;

import com.threeriversbank.banking.model.Transaction;
import com.threeriversbank.banking.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @InjectMocks
    private TransactionService transactionService;
    
    private Transaction transaction1;
    private Transaction transaction2;
    
    @BeforeEach
    void setUp() {
        transaction1 = new Transaction(
            "1001234567",
            "2001234567",
            new BigDecimal("1000.00"),
            "TRANSFER",
            LocalDateTime.now(),
            "Test transfer 1"
        );
        transaction1.setId(1L);
        
        transaction2 = new Transaction(
            "2001234567",
            "1001234567",
            new BigDecimal("500.00"),
            "TRANSFER",
            LocalDateTime.now().minusHours(1),
            "Test transfer 2"
        );
        transaction2.setId(2L);
    }
    
    @Test
    void testGetTransactionsByAccountNumber_Success() {
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findByFromAccountNumberOrToAccountNumberOrderByTimestampDesc(
            "1001234567", "1001234567")).thenReturn(transactions);
        
        List<Transaction> result = transactionService.getTransactionsByAccountNumber("1001234567");
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(transaction1.getId(), result.get(0).getId());
        assertEquals(transaction2.getId(), result.get(1).getId());
        verify(transactionRepository, times(1))
            .findByFromAccountNumberOrToAccountNumberOrderByTimestampDesc("1001234567", "1001234567");
    }
    
    @Test
    void testGetTransactionsByAccountNumber_EmptyList() {
        when(transactionRepository.findByFromAccountNumberOrToAccountNumberOrderByTimestampDesc(
            "9999999999", "9999999999")).thenReturn(Arrays.asList());
        
        List<Transaction> result = transactionService.getTransactionsByAccountNumber("9999999999");
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transactionRepository, times(1))
            .findByFromAccountNumberOrToAccountNumberOrderByTimestampDesc("9999999999", "9999999999");
    }
    
    @Test
    void testCreateTransaction_Success() {
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction tx = invocation.getArgument(0);
            tx.setId(3L);
            return tx;
        });
        
        Transaction result = transactionService.createTransaction(
            "1001234567",
            "2001234567",
            new BigDecimal("250.00"),
            "TRANSFER",
            "Test description"
        );
        
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("1001234567", result.getFromAccountNumber());
        assertEquals("2001234567", result.getToAccountNumber());
        assertEquals(new BigDecimal("250.00"), result.getAmount());
        assertEquals("TRANSFER", result.getType());
        assertEquals("Test description", result.getDescription());
        assertNotNull(result.getTimestamp());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    
    @Test
    void testCreateTransaction_VerifyTimestamp() {
        LocalDateTime beforeCreation = LocalDateTime.now();
        
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction tx = invocation.getArgument(0);
            assertNotNull(tx.getTimestamp());
            assertTrue(tx.getTimestamp().isAfter(beforeCreation) || 
                      tx.getTimestamp().isEqual(beforeCreation));
            return tx;
        });
        
        transactionService.createTransaction(
            "1001234567",
            "2001234567",
            new BigDecimal("100.00"),
            "TRANSFER",
            "Timestamp test"
        );
        
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}
