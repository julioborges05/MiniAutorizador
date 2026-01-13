package com.julioborges.autorizador.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julioborges.autorizador.api.dto.NewCardRequest;
import com.julioborges.autorizador.api.dto.NewCardResponse;
import com.julioborges.autorizador.domain.service.CardService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CardControllerTest {

    private CardController cardController;

    @Mock
    private CardService cardService;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        cardController = new CardController(cardService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(cardController)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void createCard() throws Exception {
        NewCardRequest request = new NewCardRequest("123", "123");
        NewCardResponse response = new NewCardResponse("123");

        when(cardService.createCard(any(NewCardRequest.class))).thenReturn(response);

        mockMvc.perform(
                post("/cartoes")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    void getCardBalance() throws Exception {
        when(cardService.getCardBalance(anyString())).thenReturn("R$ 100,00");

        mockMvc.perform(get("/cartoes/{numeroCartao}", "123")
        ).andExpect(status().is2xxSuccessful());
    }

}
