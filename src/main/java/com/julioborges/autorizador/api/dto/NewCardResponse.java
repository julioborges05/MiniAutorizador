package com.julioborges.autorizador.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NewCardResponse(@JsonProperty("numeroCartao") String cardNumber) {
}
