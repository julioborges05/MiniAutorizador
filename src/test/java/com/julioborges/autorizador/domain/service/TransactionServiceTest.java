package com.julioborges.autorizador.domain.service;

import com.julioborges.autorizador.api.dto.NewCardRequest;
import com.julioborges.autorizador.api.dto.TransactionRequest;
import com.julioborges.autorizador.domain.authorization.CardBalanceValidation;
import com.julioborges.autorizador.domain.authorization.PasswordValidation;
import com.julioborges.autorizador.domain.model.AuthorizationFailed;
import com.julioborges.autorizador.domain.model.Card;
import com.julioborges.autorizador.domain.repository.CardRepository;
import com.julioborges.autorizador.exception.AuthorizationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private CardBalanceValidation cardBalanceValidation;

    @Mock
    private PasswordValidation passwordValidation;

    @Mock
    private CardRepository cardRepository;

    @Captor
    private ArgumentCaptor<Card> cardCaptor;

    private TransactionService transactionService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(List.of(cardBalanceValidation, passwordValidation), cardRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void sendTransaction_throwsWhenCardNotFound() {
        TransactionRequest request = new TransactionRequest("123", "123", BigDecimal.valueOf(100));

        when(cardRepository.findByCardNumberForUpdate(anyString())).thenReturn(Optional.empty());

        AuthorizationException response = assertThrows(
                AuthorizationException.class,
                () -> transactionService.sendTransaction(request)
        );

        assertEquals(AuthorizationFailed.CARD_NOT_FOUND, response.getReason());
    }

    @Test
    void sendTransaction_callValidateMethod() {
        TransactionRequest request = new TransactionRequest("123", "123", BigDecimal.valueOf(100));
        NewCardRequest newCardRequest = new NewCardRequest("123", "123");
        Card card = new Card(newCardRequest);

        when(cardRepository.findByCardNumberForUpdate(anyString())).thenReturn(Optional.of(card));

        transactionService.sendTransaction(request);

        verify(cardBalanceValidation, times(1)).validate(any(Card.class), any(TransactionRequest.class));
        verify(passwordValidation, times(1)).validate(any(Card.class), any(TransactionRequest.class));
        verify(cardRepository, times(1)).save(cardCaptor.capture());
        assertEquals(BigDecimal.valueOf(400), cardCaptor.getValue().getBalance());
    }

    @Test
    void sendTransaction_throwsWhenDebitIsHigherThanBalance() {
        TransactionRequest request = new TransactionRequest("123", "123", BigDecimal.valueOf(600));
        NewCardRequest newCardRequest = new NewCardRequest("123", "123");
        Card card = new Card(newCardRequest);

        when(cardRepository.findByCardNumberForUpdate(anyString())).thenReturn(Optional.of(card));

        AuthorizationException response = assertThrows(
                AuthorizationException.class,
                () -> transactionService.sendTransaction(request)
        );

        assertEquals(AuthorizationFailed.INSUFFICIENT_BALANCE, response.getReason());
    }

}
