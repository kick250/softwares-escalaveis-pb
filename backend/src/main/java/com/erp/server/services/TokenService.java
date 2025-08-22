package com.erp.server.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import infra.global.entities.UserEntity;
import com.erp.server.exceptions.InvalidTokenException;
import com.erp.server.infra.TimeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class TokenService {
    private final String issuer;
    private final String secret;
    private final TimeConfig timeConfig;

    public TokenService(@Value("${spring.application.name}") String issuer, @Value("${spring.application.security.token.secret}") String secret, TimeConfig timeConfig) {
        this.issuer = issuer;
        this.secret = secret;
        this.timeConfig = timeConfig;
    }

    public String generateToken(UserEntity userEntity) {
        var algorithm = Algorithm.HMAC256(secret);

        return JWT
                .create()
                .withIssuer(issuer)
                .withSubject(userEntity.getUsername())
                .withExpiresAt(this.getExpiresAT())
                .sign(algorithm);
    }

    private Instant getExpiresAT() {
        return LocalDateTime.now().plusHours(2).toInstant(timeConfig.zoneOffset());
    }

    public DecodedJWT decodeToken(String jwtToken) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(jwtToken);
        } catch (JWTVerificationException exception) {
            throw new InvalidTokenException();
        }
    }
}