package com.julioborges.autorizador.repository;

import com.julioborges.autorizador.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginAndEnabled(String login, boolean isEnabled);

}
