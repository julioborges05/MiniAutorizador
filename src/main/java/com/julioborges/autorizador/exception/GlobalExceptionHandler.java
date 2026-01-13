package com.julioborges.autorizador.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        log.warn("Erro de validação: {}", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> body = new HashMap<>();

        if (ex.getCause() != null && ex.getCause().getMessage().contains("Duplicate entry")) {
            log.warn("Violação de integridade detectada: {}", ex.getCause().getMessage());
            body.put("erro", "Informação já existe");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
        }

        throw ex;
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCardNotFoundException(CardNotFoundException ex) {
        log.info("Cartão não encontrado: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleTransactionAuthorizationFailedException(AuthorizationException ex) {
        log.warn(
                "Falha na autorização da transação. Motivo: {}",
                ex.getReason().getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(ex.getReason().getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericError(Exception ex) {
        log.error(
                "Erro interno não tratado: {}",
                ex.getMessage(),
                ex
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno no servidor");
    }

}
