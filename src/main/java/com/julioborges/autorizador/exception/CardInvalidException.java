package com.julioborges.autorizador.exception;

public class CardInvalidException extends RuntimeException {

    private final String reason;

    public CardInvalidException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
