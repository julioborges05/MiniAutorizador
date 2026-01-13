package com.julioborges.autorizador.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record NewCardRequest(
        @NotBlank(message = "O número do cartão é obrigatório")
        @JsonProperty("numeroCartao") String cardNumber,

        @NotBlank(message = "A senha é obrigatória")
        @JsonProperty("senha") String password
) {
}
