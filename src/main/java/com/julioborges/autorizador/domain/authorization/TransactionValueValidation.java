package com.julioborges.autorizador.domain.authorization;

import com.julioborges.autorizador.api.dto.TransactionRequest;
import com.julioborges.autorizador.domain.model.AuthorizationFailed;
import com.julioborges.autorizador.domain.model.Card;
import com.julioborges.autorizador.exception.AuthorizationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class TransactionValueValidation implements AuthorizationRule {
    @Override
    public void validate(Card card, TransactionRequest transactionRequest) {
        Optional.of(transactionRequest.value())
                .filter(value -> value.compareTo(BigDecimal.ZERO) > 0)
                .orElseThrow(() -> new AuthorizationException(AuthorizationFailed.INVALID_TRANSACTION_VALUE));
    }
}
