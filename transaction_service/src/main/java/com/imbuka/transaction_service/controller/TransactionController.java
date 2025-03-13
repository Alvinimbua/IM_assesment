package com.imbuka.transaction_service.controller;

import com.imbuka.transaction_service.constants.TransactionsConstants;
import com.imbuka.transaction_service.dto.ResponseDto;
import com.imbuka.transaction_service.dto.TransactionRequest;
import com.imbuka.transaction_service.service.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "REST APIs from handling transactions (withdraw, deposit,transfer) of funds in I&M Bank",
        description = "REST APIs in I&M Bank to withdraw, deposit, transfer funds"
)
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransactionService transactionService;

    @Operation(
            summary = "Deposit REST API",
            description = "REST API to deposit funds in I&M Bank account"
    )
    @ApiResponse(
            responseCode = "202",
            description = "Deposit  successfully"
    )
    @PostMapping("/deposit")
    public ResponseEntity<ResponseDto> deposit(@RequestBody TransactionRequest request) {
        transactionService.deposit(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(TransactionsConstants.STATUS_202, TransactionsConstants.MESSAGE_202));
    }

    @Operation(
            summary = "Withdraw REST API",
            description = "REST API to withdraw funds in I&M Bank account"
    )
    @ApiResponse(
            responseCode = "203",
            description = "Withdraw  successfully"
    )
    @PostMapping("/withdraw")
    public ResponseEntity<ResponseDto> withdraw(@RequestBody TransactionRequest request) {
        transactionService.withdraw(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(TransactionsConstants.STATUS_203, TransactionsConstants.MESSAGE_203));
    }

    @Operation(
            summary = "Transfer REST API",
            description = "REST API to transfer funds in I&M Bank account"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Transfer  successfully"
    )
    @PostMapping("/transfer")
    public ResponseEntity<ResponseDto> transfer(@RequestBody TransactionRequest request) {
        transactionService.transfer(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(TransactionsConstants.STATUS_204, TransactionsConstants.MESSAGE_204));
    }

}
