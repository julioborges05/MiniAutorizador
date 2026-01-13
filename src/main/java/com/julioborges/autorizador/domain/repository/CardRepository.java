package com.julioborges.autorizador.domain.repository;

import com.julioborges.autorizador.domain.model.Card;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {

    Optional<Card> findByCardNumber(String cardNumber);

    @Query(value = " SELECT c.* FROM card c WHERE c.card_number = :cardNumber FOR UPDATE", nativeQuery = true)
    Optional<Card> findByCardNumberForUpdate(String cardNumber);

}
