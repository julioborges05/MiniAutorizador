package com.julioborges.autorizador.domain.service;

import com.julioborges.autorizador.domain.model.User;
import com.julioborges.autorizador.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.encoder = new BCryptPasswordEncoder();
        this.userRepository = userRepository;
    }

    public boolean validateLogin(String login, String password) {
        User user = userRepository.findByLoginAndEnabled(login, true)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return encoder.matches(password, user.getPassword());
    }

}
