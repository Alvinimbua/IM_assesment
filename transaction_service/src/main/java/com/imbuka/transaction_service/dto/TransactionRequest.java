package com.imbuka.transaction_service.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private Long accountNumber;
    private Double amount;
    private Long targetAccountNumber;

}
