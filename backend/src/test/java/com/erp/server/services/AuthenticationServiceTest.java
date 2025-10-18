package com.erp.server.services;

import infra.global.relational.entities.UserEntity;
import infra.global.relational.repositories.UsersJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    private UsersJpaRepository usersJpaRepository;

    @BeforeEach
    public void setUp() {
        usersJpaRepository = mock(UsersJpaRepository.class);
        authenticationService = new AuthenticationService(usersJpaRepository);
    }

    @Test
    public void testLoadUserByUsername_whenUserExists() {
        String username = "test@test.com.br";

        UserEntity user = mock(UserEntity.class);
        Optional<UserEntity> optionalUser = Optional.of(user);

        when(usersJpaRepository.findByUsername(username)).thenReturn(optionalUser);

        UserDetails loadedUser = authenticationService.loadUserByUsername(username);

        assertNotNull(loadedUser);
        assertEquals(user, loadedUser);
    }

    @Test
    public void testLoadUserByUsername_whenUserDoesNotExist() {
        String username = "test@test.com.br";

        Optional<UserEntity> optionalUser = Optional.empty();

        when(usersJpaRepository.findByUsername(username)).thenReturn(optionalUser);

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> authenticationService.loadUserByUsername(username));
        assertEquals("Usuário não encontrado: " + username, exception.getMessage());
    }
}