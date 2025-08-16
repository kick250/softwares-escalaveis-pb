package com.erp.server.controllers;

import com.erp.server.entities.User;
import com.erp.server.requests.LoginRequest;
import com.erp.server.responses.TokenResponse;
import com.erp.server.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            var token = new UsernamePasswordAuthenticationToken(request.username(), request.password());
            var authentication = this.authenticationManager.authenticate(token);

            String jwtToken = this.tokenService.generateToken((User) authentication.getPrincipal());
            return ResponseEntity.ok(new TokenResponse(jwtToken));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
