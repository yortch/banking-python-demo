package com.threeriversbank.banking.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Schema(description = "Bank account entity")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Account ID", example = "1")
    private Long id;
    
    @Column(nullable = false, unique = true)
    @Schema(description = "Unique account number", example = "ACC001")
    private String accountNumber;
    
    @Column(nullable = false)
    @Schema(description = "Type of account", example = "Checking")
    private String accountType;
    
    @Column(nullable = false)
    @Schema(description = "Current account balance", example = "5000.00")
    private BigDecimal balance;
    
    @Column(nullable = false)
    @Schema(description = "Name of the account holder", example = "John Smith")
    private String customerName;
    
    public Account() {
    }
    
    public Account(String accountNumber, String accountType, BigDecimal balance, String customerName) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.customerName = customerName;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
