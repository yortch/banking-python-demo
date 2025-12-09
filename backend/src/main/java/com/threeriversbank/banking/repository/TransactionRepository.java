package com.threeriversbank.banking.repository;

import com.threeriversbank.banking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountNumberOrToAccountNumberOrderByTimestampDesc(String fromAccountNumber, String toAccountNumber);
}
