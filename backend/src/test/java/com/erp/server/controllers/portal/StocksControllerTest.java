package com.erp.server.controllers.portal;

import com.erp.server.factories.StocksFactory;
import com.erp.server.factories.UsersFactory;
import com.erp.server.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import infra.global.relational.entities.StockEntity;
import infra.global.relational.entities.UserEntity;
import infra.global.relational.repositories.StocksJpaRepository;
import infra.global.relational.repositories.UsersJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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
    private UsersJpaRepository usersJpaRepository;
    @Autowired
    private StocksJpaRepository stocksJpaRepository;
    @Autowired
    private TokenService tokenService;

    UserEntity currentUser;
    String token;
    StockEntity stock1;
    StockEntity stock2;

    @BeforeEach
    void setUp() {
        stocksJpaRepository.deleteAll();
        usersJpaRepository.deleteAll();

        currentUser = usersFactory.createPortalUser();
        usersJpaRepository.save(currentUser);
        token = "Bearer " + tokenService.generateToken(currentUser);

        stock1 = stocksFactory.createStock();
        stocksJpaRepository.save(stock1);
        stock2 = stocksFactory.createStock();
        stocksJpaRepository.save(stock2);
    }

    @AfterEach
    void tearDown() {
        stocksJpaRepository.deleteAll();
        usersJpaRepository.deleteAll();
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