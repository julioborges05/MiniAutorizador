package com.julioborges.autorizador.domain.model;

import com.julioborges.autorizador.api.dto.NewCardRequest;
import com.julioborges.autorizador.api.dto.NewCardResponse;
import com.julioborges.autorizador.exception.AuthorizationException;
import com.julioborges.autorizador.exception.CardInvalidException;
import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "card")
public class Card {

    @Id
    @Column(name = "card_number", nullable = false, length = 19)
    private String cardNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public Card() {
    }

    public Card(NewCardRequest newCardRequest) {
        validateNewCardFields(newCardRequest);

        this.cardNumber = newCardRequest.cardNumber();
        this.password = convertCardPassword(newCardRequest.password());
        this.balance = BigDecimal.valueOf(500);
    }

    private void validateNewCardFields(NewCardRequest newCardRequest) {
        Optional.ofNullable(newCardRequest)
                .orElseThrow(() -> new CardInvalidException("Request de cartão nulo"));

        Optional.ofNullable(newCardRequest.cardNumber())
                .orElseThrow(() -> new CardInvalidException("Número do cartão inválido"));

        Optional.ofNullable(newCardRequest.password())
                .orElseThrow(() -> new CardInvalidException("Senha do cartão vazia"));
    }

    private String convertCardPassword(String password) {
        String safetyPassword = Objects.requireNonNull(password);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(safetyPassword);
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void debitValue(BigDecimal value) {
        this.balance = Optional.ofNullable(value)
                .filter(v -> this.balance.subtract(v).compareTo(BigDecimal.ZERO) >= 0)
                .map(v -> this.balance.subtract(v))
                .orElseThrow(() -> new AuthorizationException(AuthorizationFailed.INSUFFICIENT_BALANCE));
    }

    public NewCardResponse toNewCardResponse() {
        return new NewCardResponse(this.cardNumber);
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber='" + cardNumber + '\'' +
                ", balance=" + balance +
                '}';
    }
}
