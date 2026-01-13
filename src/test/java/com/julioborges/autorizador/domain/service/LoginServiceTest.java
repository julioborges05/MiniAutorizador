package com.julioborges.autorizador.domain.service;

import com.julioborges.autorizador.domain.model.User;
import com.julioborges.autorizador.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    private LoginService loginService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        loginService = new LoginService(userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void validateLogin_throwsExceptionWhenUserNotFound() {
        when(userRepository.findByLoginAndEnabled(anyString(), anyBoolean())).thenReturn(Optional.empty());

        UsernameNotFoundException response = assertThrows(
                UsernameNotFoundException.class,
                () -> loginService.validateLogin("username", "password")
        );

        assertEquals("Usuário não encontrado", response.getMessage());
    }

    @Test
    void validateLogin_success() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setPassword(encoder.encode("password"));

        when(userRepository.findByLoginAndEnabled(anyString(), anyBoolean())).thenReturn(Optional.of(user));

        boolean response = loginService.validateLogin("username", "password");

        assertTrue(response);
    }

}
