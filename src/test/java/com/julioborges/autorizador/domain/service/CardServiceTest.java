package com.julioborges.autorizador.domain.service;

import com.julioborges.autorizador.api.dto.NewCardRequest;
import com.julioborges.autorizador.api.dto.NewCardResponse;
import com.julioborges.autorizador.domain.model.Card;
import com.julioborges.autorizador.domain.repository.CardRepository;
import com.julioborges.autorizador.exception.CardInvalidException;
import com.julioborges.autorizador.exception.CardNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Captor
    private ArgumentCaptor<Card> cardCaptor;

    private CardService cardService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        cardService = new CardService(cardRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void createCard_throwsWhenCardRequestIsNull() {
        CardInvalidException response = assertThrows(
                CardInvalidException.class,
                () -> cardService.createCard(null)
        );

        assertEquals("Request de cartão nulo", response.getReason());
    }

    @Test
    void createCard_throwsWhenCardNumberIsNull() {
        NewCardRequest request = new NewCardRequest(null, "123");

        CardInvalidException response = assertThrows(
                CardInvalidException.class,
                () -> cardService.createCard(request)
        );

        assertEquals("Número do cartão inválido", response.getReason());
    }

    @Test
    void createCard_throwsWhenCardPasswordIsNull() {
        NewCardRequest request = new NewCardRequest("123", null);

        CardInvalidException response = assertThrows(
                CardInvalidException.class,
                () -> cardService.createCard(request)
        );

        assertEquals("Senha do cartão vazia", response.getReason());
    }

    @Test
    void createCard_success() {
        NewCardRequest request = new NewCardRequest("123456", "123");
        Card savedCardMock = new Card(request);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        when(cardRepository.save(any(Card.class))).thenReturn(savedCardMock);

        NewCardResponse response = cardService.createCard(request);

        assertEquals("123456", response.cardNumber());
        verify(cardRepository, times(1)).save(cardCaptor.capture());
        assertTrue(encoder.matches("123", cardCaptor.getValue().getPassword()));
        assertEquals(BigDecimal.valueOf(500), cardCaptor.getValue().getBalance());
    }

    @Test
    void getCardBalance_throwsWhenCardNotFound() {
        when(cardRepository.findByCardNumber(anyString())).thenReturn(Optional.empty());

        CardNotFoundException response = assertThrows(
                CardNotFoundException.class,
                () -> cardService.getCardBalance("123")
        );

        assertEquals("Cartão não encontrado", response.getMessage());
    }

    @Test
    void getCardBalance_success() {
        NewCardRequest newCardRequest = new NewCardRequest("123", "123");
        Card card = new Card(newCardRequest);

        when(cardRepository.findByCardNumber(anyString())).thenReturn(Optional.of(card));

        String response = cardService.getCardBalance("123");

        assertEquals("R$ 500,00", response);
    }

}
