package com.julioborges.autorizador.domain.authorization;

import com.julioborges.autorizador.api.dto.NewCardRequest;
import com.julioborges.autorizador.api.dto.TransactionRequest;
import com.julioborges.autorizador.domain.model.AuthorizationFailed;
import com.julioborges.autorizador.domain.model.Card;
import com.julioborges.autorizador.exception.AuthorizationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidationTest {

    private final PasswordValidation passwordValidation = new PasswordValidation();

    @Test
    void fail() {
        NewCardRequest newCardRequest = new NewCardRequest("123", "123");
        Card card = new Card(newCardRequest);

        TransactionRequest transactionRequest = new TransactionRequest("123", "1234", BigDecimal.valueOf(400));

        AuthorizationException response = assertThrows(
                AuthorizationException.class,
                () -> passwordValidation.validate(card, transactionRequest)
        );

        assertEquals(AuthorizationFailed.INVALID_PASSWORD, response.getReason());
    }

    @Test
    void success() {
        NewCardRequest newCardRequest = new NewCardRequest("123", "123");
        Card card = new Card(newCardRequest);

        TransactionRequest transactionRequest = new TransactionRequest("123", "123", BigDecimal.valueOf(200));

        assertDoesNotThrow(
                () -> passwordValidation.validate(card, transactionRequest)
        );
    }
}
