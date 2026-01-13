package com.julioborges.autorizador.domain.model;

public enum AuthorizationFailed {
    INSUFFICIENT_BALANCE("Saldo insuficiente"),
    INVALID_PASSWORD("Senha inválida"),
    CARD_NOT_FOUND("Cartão inexistente"),
    INVALID_TRANSACTION_VALUE("Valor de transação inválido");

    private final String messagePt;

    AuthorizationFailed(String messagePt) {
        this.messagePt = messagePt;
    }

    public String getMessage() {
        return messagePt;
    }
}
