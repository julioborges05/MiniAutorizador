package com.julioborges.autorizador.exception;

import com.julioborges.autorizador.domain.model.AuthorizationFailed;

public class AuthorizationException extends RuntimeException {

    private final AuthorizationFailed reason;

    public AuthorizationException(AuthorizationFailed reason) {
        super(reason.getMessage());
        this.reason = reason;
    }

    public AuthorizationFailed getReason() {
        return reason;
    }
}
