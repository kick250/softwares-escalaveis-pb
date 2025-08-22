package com.erp.server.infra;

import infra.global.repositories.UsersRepository;
import com.erp.server.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final UsersRepository userRepository;
    private final TokenService tokenService;

    public SecurityFilter(UsersRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = this.getToken(request);

        if (!jwtToken.isBlank()) {
            var decodedToken = tokenService.decodeToken(jwtToken);
            this.authenticate(decodedToken.getSubject());
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null)
            return "";

        return token.replace("Bearer ", "");
    }

    private void authenticate(String subject) {
        this.userRepository.findByUsername(subject).ifPresent(user -> {
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        });
    }
}