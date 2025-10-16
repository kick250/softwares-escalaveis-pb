package com.erp.server.controllers.portal;

import com.erp.server.factories.*;
import com.erp.server.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import infra.global.entities.*;
import infra.global.repositories.*;
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
class ProductsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UsersFactory usersFactory = new UsersFactory();
    private final StocksFactory stocksFactory = new StocksFactory();
    private final StockItemsFactory stockItemsFactory = new StockItemsFactory();
    private final ProductsFactory productsFactory = new ProductsFactory();
    private final AttachmentsFactory attachmentsFactory = new AttachmentsFactory();

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private StocksRepository stocksRepository;
    @Autowired
    private StockItemsRepository stockItemsRepository;
    @Autowired
    private ProductsRepository productsRepository;
    @Autowired
    private AttachmentsRepository attachmentsRepository;
    @Autowired
    private TokenService tokenService;

    private UserEntity currentUser;
    private String token;

    private StockEntity stock1;
    private StockItemEntity stockItem1;
    private StockItemEntity stockItem2;
    private ProductEntity product1;
    private ProductEntity product2;

    @BeforeEach
    void setUp() {
        stockItemsRepository.deleteAll();
        productsRepository.deleteAll();
        stocksRepository.deleteAll();
        usersRepository.deleteAll();
        attachmentsRepository.deleteAll();

        currentUser = usersFactory.createPortalUser();
        usersRepository.save(currentUser);
        token = "Bearer " + tokenService.generateToken(currentUser);

        stock1 = stocksFactory.createStock();
        stocksRepository.save(stock1);
        StockEntity stock2 = stocksFactory.createStock();
        stocksRepository.save(stock2);

        AttachmentEntity attachmentEntity1 = attachmentsFactory.createAttachment();
        attachmentsRepository.save(attachmentEntity1);
        AttachmentEntity attachmentEntity2 = attachmentsFactory.createAttachment();
        attachmentsRepository.save(attachmentEntity2);

        product1 = productsFactory.createProduct();
        product1.setAttachmentEntity(attachmentEntity1);
        productsRepository.save(product1);
        product2 = productsFactory.createProduct();
        product2.setAttachmentEntity(attachmentEntity2);
        productsRepository.save(product2);

        stockItem1 = stockItemsFactory.createStockItemWithProduct(product1);
        stockItemsRepository.save(stockItem1);
        stockItem2 = stockItemsFactory.createStockItemWithProduct(product2);
        stockItemsRepository.save(stockItem2);

        stock1.addStockItem(stockItem1);
        stock1.addStockItem(stockItem2);

        stocksRepository.save(stock1);

        AttachmentEntity attachmentEntity3 = attachmentsFactory.createAttachment();
        attachmentsRepository.save(attachmentEntity3);
        ProductEntity product3 = productsFactory.createProduct();
        product3.setAttachmentEntity(attachmentEntity3);
        productsRepository.save(product3);
        StockItemEntity stockItem3 = stockItemsFactory.createStockItemWithProduct(product3);
        stockItemsRepository.save(stockItem3);
        stock2.addStockItem(stockItem3);
        stocksRepository.save(stock2);
    }

    @AfterEach
    void tearDown() {
        stockItemsRepository.deleteAll();
        productsRepository.deleteAll();
        stocksRepository.deleteAll();
        usersRepository.deleteAll();
        attachmentsRepository.deleteAll();
    }

    @Test
    public void testGetAll() throws Exception {
        assertEquals(3, stockItemsRepository.count());

        mockMvc.perform(get("/portal/products?stockId=" + stock1.getId())
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].id").value(stockItem1.getId()))
                .andExpect(jsonPath("$.[0].name").value(product1.getName()))
                .andExpect(jsonPath("$.[0].description").value(product1.getDescription()))
                .andExpect(jsonPath("$.[0].price").value(stockItem1.getPrice()))
                .andExpect(jsonPath("$.[0].quantityAvailable").value(stockItem1.getQuantity()))
                .andExpect(jsonPath("$.[0].imagePath").value("/products/" + product1.getId() + "/image"))
                .andExpect(jsonPath("$.[1].id").value(stockItem2.getId()))
                .andExpect(jsonPath("$.[1].name").value(product2.getName()))
                .andExpect(jsonPath("$.[1].description").value(product2.getDescription()))
                .andExpect(jsonPath("$.[1].price").value(stockItem2.getPrice()))
                .andExpect(jsonPath("$.[1].quantityAvailable").value(stockItem2.getQuantity()))
                .andExpect(jsonPath("$.[1].imagePath").value("/products/" + product2.getId() + "/image"));
    }
}