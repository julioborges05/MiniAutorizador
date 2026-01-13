package com.julioborges.autorizador.domain.authorization;

import com.julioborges.autorizador.api.dto.NewCardRequest;
import com.julioborges.autorizador.api.dto.TransactionRequest;
import com.julioborges.autorizador.domain.model.AuthorizationFailed;
import com.julioborges.autorizador.domain.model.Card;
import com.julioborges.autorizador.exception.AuthorizationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionValueValidationTest {

    private final TransactionValueValidation transactionValueValidation = new TransactionValueValidation();

    @Test
    void fail() {
        NewCardRequest newCardRequest = new NewCardRequest("123", "123");
        Card card = new Card(newCardRequest);

        TransactionRequest transactionRequest = new TransactionRequest("123", "123", BigDecimal.valueOf(-400));

        AuthorizationException response = assertThrows(
                AuthorizationException.class,
                () -> transactionValueValidation.validate(card, transactionRequest)
        );

        assertEquals(AuthorizationFailed.INVALID_TRANSACTION_VALUE, response.getReason());
    }

    @Test
    void success() {
        NewCardRequest newCardRequest = new NewCardRequest("123", "123");
        Card card = new Card(newCardRequest);

        TransactionRequest transactionRequest = new TransactionRequest("123", "123", BigDecimal.valueOf(200));

        assertDoesNotThrow(
                () -> transactionValueValidation.validate(card, transactionRequest)
        );
    }
}
