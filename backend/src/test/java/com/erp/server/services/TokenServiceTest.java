package com.erp.server.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import infra.global.relational.entities.UserEntity;
import com.erp.server.infra.TimeConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenServiceTest {
    private TokenService tokenService;
    private final String issuer = "testIssuer";
    private final String secret = "testSecret";
    private TimeConfig timeConfig;

    @BeforeEach
    void setUp() {
        timeConfig = mock(TimeConfig.class);

        when(timeConfig.zoneOffset()).thenReturn(ZoneOffset.of("-03:00"));

        tokenService = new TokenService(issuer, secret, timeConfig);
    }

    @Test
    void testGenerateToken() {
        String username = "usertest@test.com.br";

        UserEntity userEntity = mock(UserEntity.class);

        when(userEntity.getUsername()).thenReturn(username);

        String token = tokenService.generateToken(userEntity);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        var algorithm = Algorithm.HMAC256(secret);
        var result = JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);

        assertEquals(username, result.getSubject());
    }

    @Test
    void testVerifyToken() {
        String username = "usertest@test.com.br";

        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getUsername()).thenReturn(username);

        String token = tokenService.generateToken(userEntity);

        var result = tokenService.decodeToken(token);
        assertNotNull(result);
        assertEquals(username, result.getSubject());
        assertEquals(issuer, result.getIssuer());
        assertNotNull(result.getExpiresAt());
    }
}