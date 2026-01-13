package com.julioborges.autorizador.domain.authorization;

import com.julioborges.autorizador.api.dto.TransactionRequest;
import com.julioborges.autorizador.domain.model.AuthorizationFailed;
import com.julioborges.autorizador.domain.model.Card;
import com.julioborges.autorizador.exception.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PasswordValidation implements AuthorizationRule {

    private static final Logger log = LoggerFactory.getLogger(PasswordValidation.class);

    @Override
    public void validate(Card card, TransactionRequest transactionRequest) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        log.info("Iniciando validação de senha para o cartao {}", transactionRequest.cardNumber());

        Optional.of(transactionRequest.password())
                .filter(requestPassword -> encoder.matches(requestPassword, card.getPassword()))
                .orElseThrow(() -> new AuthorizationException(AuthorizationFailed.INVALID_PASSWORD));
    }
}
