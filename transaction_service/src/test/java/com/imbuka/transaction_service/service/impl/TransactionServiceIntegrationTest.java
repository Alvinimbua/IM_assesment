package com.imbuka.transaction_service.service.impl;

import com.imbuka.transaction_service.dto.TransactionRequest;
import com.imbuka.transaction_service.entity.Accounts;
import com.imbuka.transaction_service.entity.Transaction;
import com.imbuka.transaction_service.exception.InsufficientFundsException;
import com.imbuka.transaction_service.exception.ResourceNotFoundException;
import com.imbuka.transaction_service.repository.AccountRepository;
import com.imbuka.transaction_service.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.h2.console.enabled=true"
})
public class TransactionServiceIntegrationTest {

    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private Accounts sourceAccount;
    private Accounts targetAccount;
    private TransactionRequest request;


    @BeforeEach
    void setUp() {
        // Clear the database before each test
        transactionRepository.deleteAll();
        accountRepository.deleteAll();

        // Create test accounts
        sourceAccount = new Accounts();
        sourceAccount.setAccountNumber(1L);
        sourceAccount.setBalance(1000.0);
        accountRepository.save(sourceAccount);

        targetAccount = new Accounts();
        targetAccount.setAccountNumber(2L);
        targetAccount.setBalance(500.0);
        accountRepository.save(targetAccount);

        // Create a transaction request
        request = new TransactionRequest();
        request.setAccountNumber(1L);
        request.setAmount(200.0);
    }

    @Test
    void deposit_ShouldIncreaseBalanceAndSaveTransaction() {
        transactionService.deposit(request);

        // Verify the account balance is updated
        Optional<Accounts> updatedAccount = accountRepository.findById(1L);
        assertTrue(updatedAccount.isPresent());
        assertEquals(1200.0, updatedAccount.get().getBalance());

        // Verify the transaction is saved
        Optional<Transaction> savedTransaction = transactionRepository.findAll().stream().findFirst();
        assertTrue(savedTransaction.isPresent());
        assertEquals("DEPOSIT", savedTransaction.get().getTransactionType());
        assertEquals(200.0, savedTransaction.get().getAmount());

        // Verify Kafka event is published
        verify(kafkaTemplate, times(1)).send(eq("transaction-events"), anyString());
    }

    @Test
    void deposit_ShouldThrowException_WhenAmountIsZeroOrNegative() {
        request.setAmount(0.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.deposit(request);
        });

        assertEquals("Deposit amount must be greater than zero", exception.getMessage());
    }

    @Test
    void withdraw_ShouldDecreaseBalanceAndSaveTransaction() {
        transactionService.withdraw(request);

        // Verify the account balance is updated
        Optional<Accounts> updatedAccount = accountRepository.findById(1L);
        assertTrue(updatedAccount.isPresent());
        assertEquals(800.0, updatedAccount.get().getBalance());

        // Verify the transaction is saved
        Optional<Transaction> savedTransaction = transactionRepository.findAll().stream().findFirst();
        assertTrue(savedTransaction.isPresent());
        assertEquals("WITHDRAW", savedTransaction.get().getTransactionType());
        assertEquals(200.0, savedTransaction.get().getAmount());

        // Verify Kafka event is published
        verify(kafkaTemplate, times(1)).send(eq("transaction-events"), anyString());
    }

    @Test
    void withdraw_ShouldThrowException_WhenInsufficientFunds() {
        request.setAmount(1500.0);

        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            transactionService.withdraw(request);
        });

        assertEquals("Insufficient Funds: Withdraw amount must be greater than zero", exception.getMessage());
    }

    @Test
    void transfer_ShouldUpdateBothAccountsAndSaveTransaction() {
        request.setTargetAccountNumber(2L);

        transactionService.transfer(request);

        // Verify source account balance is updated
        Optional<Accounts> updatedSourceAccount = accountRepository.findById(1L);
        assertTrue(updatedSourceAccount.isPresent());
        assertEquals(800.0, updatedSourceAccount.get().getBalance());

        // Verify target account balance is updated
        Optional<Accounts> updatedTargetAccount = accountRepository.findById(2L);
        assertTrue(updatedTargetAccount.isPresent());
        assertEquals(700.0, updatedTargetAccount.get().getBalance());

        // Verify the transaction is saved
        Optional<Transaction> savedTransaction = transactionRepository.findAll().stream().findFirst();
        assertTrue(savedTransaction.isPresent());
        assertEquals("TRANSFER", savedTransaction.get().getTransactionType());
        assertEquals(200.0, savedTransaction.get().getAmount());

        // Verify Kafka event is published
        verify(kafkaTemplate, times(1)).send(eq("transaction-events"), anyString());
    }

    @Test
    void transfer_ShouldThrowException_WhenSourceAccountNotFound() {
        request.setTargetAccountNumber(2L);
        accountRepository.deleteById(1L); // Delete the source account

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.transfer(request);
        });

        assertEquals("Source Account not found with the given input data AccountNumber: '1'", exception.getMessage());
    }

    @Test
    void transfer_ShouldThrowException_WhenTargetAccountNotFound() {
        request.setTargetAccountNumber(3L); // Non-existent target account

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.transfer(request);
        });

        assertEquals("Target not found with the given input data AccountNumber: '3'", exception.getMessage());
    }
}
