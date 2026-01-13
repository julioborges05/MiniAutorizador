package com.julioborges.autorizador.domain.authorization;

import com.julioborges.autorizador.api.dto.TransactionRequest;
import com.julioborges.autorizador.domain.model.AuthorizationFailed;
import com.julioborges.autorizador.domain.model.Card;
import com.julioborges.autorizador.exception.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class CardBalanceValidation implements AuthorizationRule {

    private static final Logger log = LoggerFactory.getLogger(CardBalanceValidation.class);

    @Override
    public void validate(Card card, TransactionRequest transactionRequest) {
        log.info("Iniciando validação de saldo para realizar a transacao de {} para o cartão {}",
                transactionRequest.value(), transactionRequest.cardNumber());

        Optional.ofNullable(transactionRequest.value())
                .filter(v -> card.getBalance().subtract(v).compareTo(BigDecimal.ZERO) >= 0)
                .orElseThrow(() -> new AuthorizationException(AuthorizationFailed.INSUFFICIENT_BALANCE));
    }
}
