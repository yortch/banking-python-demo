package com.threeriversbank.banking.config;

import com.threeriversbank.banking.model.Account;
import com.threeriversbank.banking.model.Transaction;
import com.threeriversbank.banking.repository.AccountRepository;
import com.threeriversbank.banking.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    
    public DataLoader(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }
    
    @Override
    public void run(String... args) {
        // Create sample accounts for John Doe
        Account checking = new Account("1001234567", "Checking", new BigDecimal("5250.00"), "John Doe");
        Account savings = new Account("2001234567", "Savings", new BigDecimal("15000.00"), "John Doe");
        Account credit = new Account("3001234567", "Credit Card", new BigDecimal("-1250.00"), "John Doe");
        
        accountRepository.save(checking);
        accountRepository.save(savings);
        accountRepository.save(credit);
        
        // Create sample transactions
        Transaction t1 = new Transaction(
                "1001234567",
                "External",
                new BigDecimal("2500.00"),
                "DEPOSIT",
                LocalDateTime.now().minusDays(5),
                "Salary Deposit"
        );
        
        Transaction t2 = new Transaction(
                "1001234567",
                "External",
                new BigDecimal("150.00"),
                "WITHDRAWAL",
                LocalDateTime.now().minusDays(4),
                "Grocery Store"
        );
        
        Transaction t3 = new Transaction(
                "1001234567",
                "2001234567",
                new BigDecimal("1000.00"),
                "TRANSFER",
                LocalDateTime.now().minusDays(3),
                "Transfer to Savings"
        );
        
        Transaction t4 = new Transaction(
                "External",
                "2001234567",
                new BigDecimal("500.00"),
                "DEPOSIT",
                LocalDateTime.now().minusDays(2),
                "Interest Payment"
        );
        
        Transaction t5 = new Transaction(
                "1001234567",
                "External",
                new BigDecimal("75.50"),
                "WITHDRAWAL",
                LocalDateTime.now().minusDays(1),
                "Restaurant"
        );
        
        transactionRepository.save(t1);
        transactionRepository.save(t2);
        transactionRepository.save(t3);
        transactionRepository.save(t4);
        transactionRepository.save(t5);
        
        System.out.println("Sample data loaded successfully!");
    }
}
