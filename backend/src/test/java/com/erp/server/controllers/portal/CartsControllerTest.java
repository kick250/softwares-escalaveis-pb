package com.erp.server.controllers.portal;

import application.orders.domain.User;
import com.erp.server.factories.UsersFactory;
import com.erp.server.services.TokenService;
import infra.global.document.entities.Cart;
import infra.global.document.repositories.CartsRepository;
import infra.global.relational.entities.UserEntity;
import infra.global.relational.repositories.UsersJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CartsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final UsersFactory usersFactory = new UsersFactory();

    @Autowired
    private UsersJpaRepository usersJpaRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CartsRepository cartsRepository;

    private UserEntity currentUser;
    private String token;

    @BeforeEach
    void setUp() {
        usersJpaRepository.deleteAll();
        cartsRepository.deleteAll();

        currentUser = usersFactory.createPortalUser();
        usersJpaRepository.save(currentUser);
        token = "Bearer " + tokenService.generateToken(currentUser);
    }

    @AfterEach
    void tearDown() {
        usersJpaRepository.deleteAll();
        cartsRepository.deleteAll();
    }

    @Test
    public void testGetCart() throws Exception {
        assertEquals(cartsRepository.count(), 0);

        mockMvc.perform(get("/portal/carts")
                        .header("Authorization", token))
                .andExpect(status().isOk());

        assertEquals(cartsRepository.count(), 1);

        Cart cart = cartsRepository.findByUserIdAndActiveTrue(currentUser.getId()).orElseThrow();
        assertNotNull(cart);
        assertEquals(cart.getUserId(), currentUser.getId());
    }
}