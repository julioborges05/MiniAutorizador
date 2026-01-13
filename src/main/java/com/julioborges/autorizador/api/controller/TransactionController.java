package com.julioborges.autorizador.api.controller;

import com.julioborges.autorizador.api.dto.TransactionRequest;
import com.julioborges.autorizador.domain.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transacoes")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<String> sendTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        transactionService.sendTransaction(transactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

}
