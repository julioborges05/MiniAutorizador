package com.julioborges.autorizador.domain.authorization;

import com.julioborges.autorizador.api.dto.NewCardRequest;
import com.julioborges.autorizador.api.dto.TransactionRequest;
import com.julioborges.autorizador.domain.model.AuthorizationFailed;
import com.julioborges.autorizador.domain.model.Card;
import com.julioborges.autorizador.exception.AuthorizationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CardBalanceValidationTest {

    private final CardBalanceValidation cardBalanceValidation = new CardBalanceValidation();

    @Test
    void fail() {
        NewCardRequest newCardRequest = new NewCardRequest("123", "123");
        Card card = new Card(newCardRequest);

        TransactionRequest transactionRequest = new TransactionRequest("123", "123", BigDecimal.valueOf(600));

        AuthorizationException response = assertThrows(
                AuthorizationException.class,
                () -> cardBalanceValidation.validate(card, transactionRequest)
        );

        assertEquals(AuthorizationFailed.INSUFFICIENT_BALANCE, response.getReason());
    }

    @Test
    void success() {
        NewCardRequest newCardRequest = new NewCardRequest("123", "123");
        Card card = new Card(newCardRequest);

        TransactionRequest transactionRequest = new TransactionRequest("123", "123", BigDecimal.valueOf(200));

        assertDoesNotThrow(
                () -> cardBalanceValidation.validate(card, transactionRequest)
        );
    }

}
