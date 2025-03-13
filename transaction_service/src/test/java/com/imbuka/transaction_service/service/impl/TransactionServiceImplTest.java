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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceUnitTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Accounts sourceAccount;
    private Accounts targetAccount;
    private TransactionRequest request;

    @BeforeEach
    void setUp() {
        sourceAccount = new Accounts();
        sourceAccount.setAccountNumber(1L);
        sourceAccount.setBalance(1000.0);

        targetAccount = new Accounts();
        targetAccount.setAccountNumber(2L);
        targetAccount.setBalance(500.0);

        request = new TransactionRequest();
        request.setAccountNumber(1L);
        request.setAmount(200.0);
    }

    @Test
    void deposit_ShouldIncreaseBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));

        transactionService.deposit(request);

        assertEquals(1200.0, sourceAccount.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
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
    void withdraw_ShouldDecreaseBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));

        transactionService.withdraw(request);

        assertEquals(800.0, sourceAccount.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(kafkaTemplate, times(1)).send(eq("transaction-events"), anyString());
    }

    @Test
    void withdraw_ShouldThrowException_WhenInsufficientFunds() {
        request.setAmount(1500.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));

        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            transactionService.withdraw(request);
        });

        assertEquals("Insufficient Funds: Withdraw amount must be greater than zero", exception.getMessage());
    }

    @Test
    void transfer_ShouldUpdateBothAccounts() {
        request.setTargetAccountNumber(2L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(targetAccount));

        transactionService.transfer(request);

        assertEquals(800.0, sourceAccount.getBalance());
        assertEquals(700.0, targetAccount.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(kafkaTemplate, times(1)).send(eq("transaction-events"), anyString());
    }

    @Test
    void transfer_ShouldThrowException_WhenSourceAccountNotFound() {
        request.setTargetAccountNumber(2L);
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.transfer(request);
        });

        assertEquals("Source Account not found with the given input data AccountNumber: '1'", exception.getMessage());
    }

    @Test
    void transfer_ShouldThrowException_WhenTargetAccountNotFound() {
        request.setTargetAccountNumber(2L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.transfer(request);
        });

        assertEquals("Target not found with the given input data AccountNumber: '2'", exception.getMessage());
    }
}