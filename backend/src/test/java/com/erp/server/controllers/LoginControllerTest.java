package com.erp.server.controllers;

import com.erp.server.entities.User;
import com.erp.server.factories.UsersFactory;
import com.erp.server.repositories.UsersRepository;
import com.erp.server.requests.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private User user;

    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    public void setUp() {
        usersRepository.deleteAll();

        UsersFactory usersFactory = new UsersFactory();
        user = usersFactory.createUser();

        usersRepository.save(user);
    }

    @Test
    public void testLogin() throws Exception {
        var body = new LoginRequest(user.getUsername(), UsersFactory.DEFAULT_PASSWORD);

        mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
    }

    @Test
    public void testLogin_whenIncorrectUsername() throws Exception {
        var body = new LoginRequest("user@naoexiste.com.br", UsersFactory.DEFAULT_PASSWORD);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testLogin_whenIncorrectPassword() throws Exception {
        var body = new LoginRequest(user.getUsername(), "batata");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
    }
}