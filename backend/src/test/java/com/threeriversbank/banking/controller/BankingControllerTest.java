package com.threeriversbank.banking.controller;

import com.threeriversbank.banking.model.Account;
import com.threeriversbank.banking.service.BankingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankingController.class)
class BankingControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private BankingService bankingService;
    
    @Test
    void testGetAllAccounts() throws Exception {
        Account account1 = new Account("1001234567", "Checking", new BigDecimal("5000.00"), "John Doe");
        Account account2 = new Account("2001234567", "Savings", new BigDecimal("10000.00"), "John Doe");
        List<Account> accounts = Arrays.asList(account1, account2);
        
        when(bankingService.getAllAccounts()).thenReturn(accounts);
        
        mockMvc.perform(get("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("1001234567"))
                .andExpect(jsonPath("$[1].accountNumber").value("2001234567"));
    }
    
    @Test
    void testGetAccountByNumber() throws Exception {
        Account account = new Account("1001234567", "Checking", new BigDecimal("5000.00"), "John Doe");
        
        when(bankingService.getAccountByNumber("1001234567")).thenReturn(account);
        
        mockMvc.perform(get("/api/accounts/1001234567")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("1001234567"))
                .andExpect(jsonPath("$.accountType").value("Checking"));
    }
}
