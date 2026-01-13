package com.julioborges.autorizador.domain.repository;

import com.julioborges.autorizador.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginAndEnabled(String login, boolean isEnabled);

}
