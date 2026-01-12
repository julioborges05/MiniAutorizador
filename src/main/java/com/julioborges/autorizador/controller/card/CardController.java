package com.julioborges.autorizador.controller.card;

import com.julioborges.autorizador.controller.card.request.NewCardRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cartoes")
public class CardController {

    //TODO: Implementar criação de cartão e consulta de saldo

    @GetMapping
    public ResponseEntity<NewCardRequest> receiveCardRequest(@Valid  @RequestBody NewCardRequest newCardRequest) {
        return ResponseEntity.ok(newCardRequest);
    }

}
