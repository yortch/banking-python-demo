package com.threeriversbank.banking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Request object for transferring funds between accounts")
public class TransferRequest {
    @Schema(description = "Source account number", example = "ACC001", required = true)
    private String fromAccount;
    
    @Schema(description = "Destination account number", example = "ACC002", required = true)
    private String toAccount;
    
    @Schema(description = "Transfer amount", example = "100.00", required = true)
    private BigDecimal amount;
    
    @Schema(description = "Optional transfer description", example = "Monthly payment")
    private String description;
    
    public TransferRequest() {
    }
    
    public TransferRequest(String fromAccount, String toAccount, BigDecimal amount, String description) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.description = description;
    }
    
    public String getFromAccount() {
        return fromAccount;
    }
    
    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }
    
    public String getToAccount() {
        return toAccount;
    }
    
    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
