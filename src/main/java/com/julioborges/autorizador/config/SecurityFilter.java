package com.julioborges.autorizador.config;

import com.julioborges.autorizador.domain.service.LoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC = "Basic ";

    private final LoginService loginService;

    public SecurityFilter(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerAuth = request.getHeader(AUTHORIZATION);

        if (headerAuth == null || !headerAuth.startsWith(BASIC)) {
            filterChain.doFilter(request, response);
            return;
        }

        String basicToken = headerAuth.replace(BASIC, "");
        String basicTokenValue = new String(Base64.getDecoder().decode(basicToken));
        String login = basicTokenValue.split(":")[0];
        String password = basicTokenValue.split(":")[1];

        if (loginService.validateLogin(login, password)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(login, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}
