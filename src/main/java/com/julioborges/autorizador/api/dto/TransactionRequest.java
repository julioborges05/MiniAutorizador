package com.julioborges.autorizador.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotBlank(message = "O número do cartão é obrigatório")
        @JsonProperty("numeroCartao") String cardNumber,

        @NotBlank(message = "A senha é obrigatória")
        @JsonProperty("senha") String password,

        @NotNull(message = "O valor da transação é obrigatório")
        @JsonProperty("valor") BigDecimal value
) {
}
