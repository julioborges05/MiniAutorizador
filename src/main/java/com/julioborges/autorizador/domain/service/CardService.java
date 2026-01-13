package com.julioborges.autorizador.domain.service;

import com.julioborges.autorizador.api.dto.NewCardRequest;
import com.julioborges.autorizador.api.dto.NewCardResponse;
import com.julioborges.autorizador.domain.model.Card;
import com.julioborges.autorizador.domain.repository.CardRepository;
import com.julioborges.autorizador.exception.CardNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;

@Service
public class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public NewCardResponse createCard(NewCardRequest newCardRequest) {
        log.info("Iniciando criação de cartão. Request: {}", newCardRequest);

        Card newCard = new Card(newCardRequest);
        return cardRepository.save(newCard).toNewCardResponse();
    }

    public String getCardBalance(String cardNumber) {
        log.info("Iniciando consulta de saldo do cartão {}", cardNumber);

        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new CardNotFoundException("Cartão não encontrado"));

        log.info("Saldo de cartão encontrado com sucesso");

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return format.format(card.getBalance()).replace('\u00A0', ' ');
    }
}
