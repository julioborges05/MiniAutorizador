package com.julioborges.autorizador.domain.authorization;

import com.julioborges.autorizador.api.dto.TransactionRequest;
import com.julioborges.autorizador.domain.model.Card;

public interface AuthorizationRule {
    void validate(Card card, TransactionRequest transactionRequest);
}
