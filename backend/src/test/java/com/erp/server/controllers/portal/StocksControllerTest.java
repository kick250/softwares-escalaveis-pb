package com.erp.server.controllers.portal;

import com.erp.server.factories.StocksFactory;
import com.erp.server.factories.UsersFactory;
import com.erp.server.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import infra.global.entities.StockEntity;
import infra.global.entities.UserEntity;
import infra.global.repositories.StocksRepository;
import infra.global.repositories.UsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StocksControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UsersFactory usersFactory = new UsersFactory();
    private final StocksFactory stocksFactory = new StocksFactory();

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private StocksRepository stocksRepository;
    @Autowired
    private TokenService tokenService;

    UserEntity currentUser;
    String token;
    StockEntity stock1;
    StockEntity stock2;

    @BeforeEach
    void setUp() {
        stocksRepository.deleteAll();
        usersRepository.deleteAll();

        currentUser = usersFactory.createPortalUser();
        usersRepository.save(currentUser);
        token = "Bearer " + tokenService.generateToken(currentUser);

        stock1 = stocksFactory.createStock();
        stocksRepository.save(stock1);
        stock2 = stocksFactory.createStock();
        stocksRepository.save(stock2);
    }

    @AfterEach
    void tearDown() {
        stocksRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    public void testGetAll() throws  Exception {
        mockMvc.perform(get("/portal/stocks")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(stock1.getId()))
                .andExpect(jsonPath("$[0].name").value(stock1.getName()))
                .andExpect(jsonPath("$[1].id").value(stock2.getId()))
                .andExpect(jsonPath("$[1].name").value(stock2.getName()));
    }
}