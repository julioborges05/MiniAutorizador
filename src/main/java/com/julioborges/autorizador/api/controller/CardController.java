package com.julioborges.autorizador.api.controller;

import com.julioborges.autorizador.api.dto.NewCardRequest;
import com.julioborges.autorizador.api.dto.NewCardResponse;
import com.julioborges.autorizador.domain.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cartoes")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<NewCardResponse> createCard(@Valid @RequestBody NewCardRequest newCardRequest) {
        NewCardResponse response = cardService.createCard(newCardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<String> getCardBalance(@PathVariable("numeroCartao") String cardNumber) {
        String cardBalance = cardService.getCardBalance(cardNumber);
        return ResponseEntity.ok(cardBalance);
    }

}
