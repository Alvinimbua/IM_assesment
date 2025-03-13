package com.imbuka.transaction_service.service.impl;

import com.imbuka.transaction_service.dto.TransactionRequest;
import com.imbuka.transaction_service.entity.Accounts;
import com.imbuka.transaction_service.entity.Transaction;
import com.imbuka.transaction_service.exception.InsufficientFundsException;
import com.imbuka.transaction_service.exception.ResourceNotFoundException;
import com.imbuka.transaction_service.repository.AccountRepository;
import com.imbuka.transaction_service.repository.TransactionRepository;
import com.imbuka.transaction_service.service.ITransactionService;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    @Override
    public synchronized void deposit(TransactionRequest request) {
        // Validate the request amount
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        // Fetch the account
        Accounts accounts = accountRepository.findById(request.getAccountNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "AccountNumber", request.getAccountNumber().toString()));

        // Update the account balance
        accounts.setBalance(accounts.getBalance() + request.getAmount());

        // Save the transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(request.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAccount(accounts);

        try {
            transactionRepository.save(transaction);
        } catch (OptimisticLockException e) {
            throw new ConcurrentModificationException("Transaction failed due to current modification, please retry");
        }


        //publish event to kafka
        kafkaTemplate.send("transaction-events", "Deposit completed for account: " + accounts.getAccountNumber());
    }


    @Transactional
    @Override
    public synchronized void withdraw(TransactionRequest request) {
        // Fetch the account
        Accounts accounts = accountRepository.findById(request.getAccountNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "AccountNumber", request.getAccountNumber().toString()));

        if (accounts.getBalance() < request.getAmount()) {
            throw new InsufficientFundsException("Insufficient Funds: Withdraw amount must be greater than zero");
        }

        accounts.setBalance(accounts.getBalance() - request.getAmount());
        accountRepository.save(accounts);

        Transaction transaction = new Transaction();
        transaction.setTransactionType("WITHDRAW");
        transaction.setAmount(request.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAccount(accounts);
        transactionRepository.save(transaction);

        //publish event to kafka
        kafkaTemplate.send("transaction-events", "Withdrawal completed for account: " + accounts.getAccountNumber());


    }

    @Override
    public synchronized void transfer(TransactionRequest request) {
        Accounts sourceAccount = accountRepository.findById(request.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Source Account", "AccountNumber", request.getAccountNumber().toString()));

        Accounts targetAccount = accountRepository.findById(request.getTargetAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Target", "AccountNumber", request.getTargetAccountNumber().toString()));

        sourceAccount.setBalance(sourceAccount.getBalance() - request.getAmount());
        targetAccount.setBalance(targetAccount.getBalance() + request.getAmount());

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        Transaction transaction = new Transaction();
        transaction.setTransactionType("TRANSFER");
        transaction.setAmount(request.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transactionRepository.save(transaction);

        //publish event to kafka
        kafkaTemplate.send("transaction-events", "Transfer completed for account: " + sourceAccount.getAccountNumber());
    }

}
