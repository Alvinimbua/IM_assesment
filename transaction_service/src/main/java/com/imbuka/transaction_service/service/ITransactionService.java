package com.imbuka.transaction_service.service;

import com.imbuka.transaction_service.dto.TransactionRequest;

public interface ITransactionService {

    void deposit(TransactionRequest request);

    void withdraw(TransactionRequest request);

    void transfer(TransactionRequest request);
}
