package com.julioborges.autorizador.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julioborges.autorizador.api.dto.TransactionRequest;
import com.julioborges.autorizador.domain.service.TransactionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest {

    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        transactionController = new TransactionController(transactionService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(transactionController)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void createCard() throws Exception {
        TransactionRequest request = new TransactionRequest("123", "123", BigDecimal.valueOf(400));

        mockMvc.perform(
                post("/transacoes")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
    }

}
