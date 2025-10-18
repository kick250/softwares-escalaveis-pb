package com.erp.server.controllers;

import com.erp.server.factories.*;
import com.erp.server.requests.StockItemCreateRequest;
import com.erp.server.requests.StockItemUpdateRequest;
import com.erp.server.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import infra.global.relational.entities.*;
import infra.global.relational.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StockItemsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StocksFactory stocksFactory = new StocksFactory();
    private final StockItemsFactory stockItemsFactory = new StockItemsFactory();
    private final ProductsFactory productsFactory = new ProductsFactory();
    private final AttachmentsFactory attachmentsFactory = new AttachmentsFactory();

    private String token;

    @Autowired
    private UsersJpaRepository usersJpaRepository;
    @Autowired
    private StocksJpaRepository stocksJpaRepository;
    @Autowired
    private StockItemsJpaRepository stockItemsJpaRepository;
    @Autowired
    private ProductsJpaRepository productsRepository;
    @Autowired
    private AttachmentsJpaRepository attachmentsJpaRepository;
    @Autowired
    private TokenService tokenService;

    private StockEntity stockEntity1;
    private StockEntity stockEntity2;
    private StockItemEntity stockItem1;
    private StockItemEntity stockItem2;
    private StockItemEntity stockItem3;
    private ProductEntity productEntity1;
    private ProductEntity productEntity2;

    @BeforeEach
    public void setUp() {
        usersJpaRepository.deleteAll();
        stockItemsJpaRepository.deleteAll();
        stocksJpaRepository.deleteAll();
        productsRepository.deleteAll();
        attachmentsJpaRepository.deleteAll();

        UsersFactory usersFactory = new UsersFactory();
        UserEntity userEntity = usersFactory.createUser();
        usersJpaRepository.save(userEntity);

        token = "Bearer " + tokenService.generateToken(userEntity);

        stockEntity1 = stocksFactory.createStock();
        stockEntity2 = stocksFactory.createStock();

        stocksJpaRepository.save(stockEntity1);
        stocksJpaRepository.save(stockEntity2);

        AttachmentEntity attachmentEntity1 = attachmentsFactory.createAttachment();
        attachmentsJpaRepository.save(attachmentEntity1);
        AttachmentEntity attachmentEntity2 = attachmentsFactory.createAttachment();
        attachmentsJpaRepository.save(attachmentEntity2);

        productEntity1 = productsFactory.createProduct();
        productEntity1.setAttachmentEntity(attachmentEntity1);
        productsRepository.save(productEntity1);
        productEntity2 = productsFactory.createProduct();
        productEntity2.setAttachmentEntity(attachmentEntity2);
        productsRepository.save(productEntity2);

        stockItem1 = stockItemsFactory.createStockItemWithProduct(productEntity1);
        stockItemsJpaRepository.save(stockItem1);
        stockItem2 = stockItemsFactory.createStockItemWithProduct(productEntity2);
        stockItemsJpaRepository.save(stockItem2);
        stockItem3 = stockItemsFactory.createStockItemWithProduct(productEntity1);
        stockItemsJpaRepository.save(stockItem3);

        stockEntity1.addStockItem(stockItem1);
        stockEntity1.addStockItem(stockItem2);
        stockEntity2.addStockItem(stockItem3);

        stocksJpaRepository.save(stockEntity1);
        stocksJpaRepository.save(stockEntity2);
    }

    @AfterEach
    public void tearDown() {
        usersJpaRepository.deleteAll();
        stockItemsJpaRepository.deleteAll();
        stocksJpaRepository.deleteAll();
        productsRepository.deleteAll();
        attachmentsJpaRepository.deleteAll();
    }

    @Test
    public void testGetById() throws Exception {
        mockMvc.perform(get("/stock_items/" + stockItem1.getId())
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(stockItem1.getId()))
                .andExpect(jsonPath("$.price").value(stockItem1.getPrice()))
                .andExpect(jsonPath("$.quantity").value(stockItem1.getQuantity()))
                .andExpect(jsonPath("$.product.id").value(stockItem1.getProduct().getId()))
                .andExpect(jsonPath("$.product.name").value(stockItem1.getProduct().getName()))
                .andExpect(jsonPath("$.product.description").value(stockItem1.getProduct().getDescription()))
                .andExpect(jsonPath("$.product.imagePath").value("/products/" + stockItem1.getProduct().getId() + "/image"));
    }

    @Test
    public void testGetById_whenNotFound() throws Exception {
        mockMvc.perform(get("/stock_items/9999")
                .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {
        StockItemCreateRequest request = new StockItemCreateRequest(100.0, 10, productEntity2.getId(), stockEntity2.getId());

        assertEquals(3, stockItemsJpaRepository.count());

        mockMvc.perform(post("/stock_items")
                .header("Authorization", token)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("Item de estoque criado com sucesso."));

        assertEquals(4, stockItemsJpaRepository.count());
    }

    @Test
    public void testCreate_whenStockAlreadyHasProduct() throws Exception {
        StockItemCreateRequest request = new StockItemCreateRequest(100.0, 10, productEntity1.getId(), stockEntity1.getId());

        assertEquals(3, stockItemsJpaRepository.count());

        mockMvc.perform(post("/stock_items")
                .header("Authorization", token)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Esse estoque já possui um item com esse produto."));

        assertEquals(3, stockItemsJpaRepository.count());
    }

    @Test
    public void testCreate_whenInvalidPrice() throws Exception {
        StockItemCreateRequest request = new StockItemCreateRequest(-100.0, 10, productEntity2.getId(), stockEntity2.getId());

        assertEquals(3, stockItemsJpaRepository.count());

        mockMvc.perform(post("/stock_items")
                .header("Authorization", token)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        assertEquals(3, stockItemsJpaRepository.count());
    }

    @Test
    public void testCreate_whenInvalidQuantity() throws Exception {
        StockItemCreateRequest request = new StockItemCreateRequest(100.0, -10, productEntity2.getId(), stockEntity2.getId());

        assertEquals(3, stockItemsJpaRepository.count());

        mockMvc.perform(post("/stock_items")
                .header("Authorization", token)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        assertEquals(3, stockItemsJpaRepository.count());
    }

    @Test
    public void testUpdate() throws Exception {
        StockItemUpdateRequest request = new StockItemUpdateRequest(150.0, 20);

        StockItemEntity stockItem = stockItemsJpaRepository.findById(stockItem1.getId()).orElseThrow();
        assertNotEquals(request.price(), stockItem.getPrice());
        assertNotEquals(request.quantity(), stockItem.getQuantity());

        mockMvc.perform(put("/stock_items/" + stockItem1.getId())
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        stockItem = stockItemsJpaRepository.findById(stockItem1.getId()).orElseThrow();
        assertEquals(request.price(), stockItem.getPrice());
        assertEquals(request.quantity(), stockItem.getQuantity());
    }

    @Test
    public void testUpdate_whenInvalidPrice() throws Exception {
        StockItemUpdateRequest request = new StockItemUpdateRequest(-150.0, 20);

        StockItemEntity stockItem = stockItemsJpaRepository.findById(stockItem1.getId()).orElseThrow();
        assertNotEquals(request.price(), stockItem.getPrice());
        assertNotEquals(request.quantity(), stockItem.getQuantity());

        mockMvc.perform(put("/stock_items/" + stockItem1.getId())
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        stockItem = stockItemsJpaRepository.findById(stockItem1.getId()).orElseThrow();
        assertNotEquals(request.price(), stockItem.getPrice());
        assertNotEquals(request.quantity(), stockItem.getQuantity());
    }

    @Test
    public void testUpdate_whenInvalidQuantity() throws Exception {
        StockItemUpdateRequest request = new StockItemUpdateRequest(150.0, -20);

        StockItemEntity stockItem = stockItemsJpaRepository.findById(stockItem1.getId()).orElseThrow();
        assertNotEquals(request.price(), stockItem.getPrice());
        assertNotEquals(request.quantity(), stockItem.getQuantity());

        mockMvc.perform(put("/stock_items/" + stockItem1.getId())
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        stockItem = stockItemsJpaRepository.findById(stockItem1.getId()).orElseThrow();
        assertNotEquals(request.price(), stockItem.getPrice());
        assertNotEquals(request.quantity(), stockItem.getQuantity());
    }

    @Test
    public void testUpdate_whenNotFound() throws Exception {
        StockItemUpdateRequest request = new StockItemUpdateRequest(150.0, 20);

        mockMvc.perform(put("/stock_items/9999")
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        assertEquals(3, stockItemsJpaRepository.count());

        mockMvc.perform(delete("/stock_items/" + stockItem1.getId())
                .header("Authorization", token))
                .andExpect(status().isOk());

        assertEquals(3, stockItemsJpaRepository.count());
        assertEquals(2, stockItemsJpaRepository.countAllByDeletedFalse());
    }

    @Test
    public void testDelete_whenNotFound() throws Exception {
        assertEquals(3, stockItemsJpaRepository.count());

        mockMvc.perform(delete("/stock_items/9999")
                        .header("Authorization", token))
                .andExpect(status().isNotFound());

        assertEquals(3, stockItemsJpaRepository.count());
        assertEquals(3, stockItemsJpaRepository.countAllByDeletedFalse());
    }
}