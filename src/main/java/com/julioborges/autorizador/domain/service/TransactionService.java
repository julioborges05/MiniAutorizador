package com.julioborges.autorizador.domain.service;

import com.julioborges.autorizador.api.dto.TransactionRequest;
import com.julioborges.autorizador.domain.authorization.AuthorizationRule;
import com.julioborges.autorizador.domain.model.AuthorizationFailed;
import com.julioborges.autorizador.domain.model.Card;
import com.julioborges.autorizador.domain.repository.CardRepository;
import com.julioborges.autorizador.exception.AuthorizationException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final List<AuthorizationRule> rules;
    private final CardRepository cardRepository;

    public TransactionService(List<AuthorizationRule> rules, CardRepository cardRepository) {
        this.rules = rules;
        this.cardRepository = cardRepository;
    }

    @Transactional
    public void sendTransaction(@Valid TransactionRequest transactionRequest) {
        log.info("Iniciando transação no valor de {} para o cartão {}", transactionRequest.value(), transactionRequest.cardNumber());

        Card card = cardRepository.findByCardNumberForUpdate(transactionRequest.cardNumber())
                .orElseThrow(() -> new AuthorizationException(AuthorizationFailed.CARD_NOT_FOUND));

        log.info("Iniciando validações de autorização para o cartão", transactionRequest.cardNumber());
        rules.forEach(rule -> rule.validate(card, transactionRequest));
        log.info("Transação autorizada para o cartão {}", transactionRequest.cardNumber());

        card.debitValue(transactionRequest.value());
        cardRepository.save(card);
    }
}
