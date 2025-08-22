package com.erp.server.controllers;

import com.erp.server.factories.*;
import com.erp.server.requests.StockCreateRequest;
import com.erp.server.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import infra.global.entities.*;
import infra.global.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StocksControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StocksFactory stocksFactory = new StocksFactory();
    private final StockItemsFactory stockItemsFactory = new StockItemsFactory();
    private final ProductsFactory productsFactory = new ProductsFactory();
    private final AttachmentsFactory attachmentsFactory = new AttachmentsFactory();

    private String token;

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

    private StockEntity stockEntity1;
    private StockEntity stockEntity2;


    @BeforeEach
    public void setUp() {
        usersRepository.deleteAll();
        stockItemsRepository.deleteAll();
        stocksRepository.deleteAll();
        productsRepository.deleteAll();
        attachmentsRepository.deleteAll();

        UsersFactory usersFactory = new UsersFactory();
        UserEntity userEntity = usersFactory.createUser();
        usersRepository.save(userEntity);

        token = "Bearer " + tokenService.generateToken(userEntity);

        stockEntity1 = stocksFactory.createStock();
        stockEntity2 = stocksFactory.createStock();

        stocksRepository.save(stockEntity1);
        stocksRepository.save(stockEntity2);

        AttachmentEntity attachmentEntity1 = attachmentsFactory.createAttachment();
        attachmentsRepository.save(attachmentEntity1);
        AttachmentEntity attachmentEntity2 = attachmentsFactory.createAttachment();
        attachmentsRepository.save(attachmentEntity2);

        ProductEntity productEntity1 = productsFactory.createProduct();
        productEntity1.setAttachmentEntity(attachmentEntity1);
        productsRepository.save(productEntity1);
        ProductEntity productEntity2 = productsFactory.createProduct();
        productEntity2.setAttachmentEntity(attachmentEntity2);
        productsRepository.save(productEntity2);

        StockItemEntity stockItem1 = stockItemsFactory.createStockItemWithProduct(productEntity1);
        stockItemsRepository.save(stockItem1);
        StockItemEntity stockItem2 = stockItemsFactory.createStockItemWithProduct(productEntity2);
        stockItemsRepository.save(stockItem2);
        StockItemEntity stockItem3 = stockItemsFactory.createStockItemWithProduct(productEntity1);
        stockItemsRepository.save(stockItem3);

        stockEntity1.addStockItem(stockItem1);
        stockEntity1.addStockItem(stockItem2);
        stockEntity2.addStockItem(stockItem3);

        stocksRepository.save(stockEntity1);
        stocksRepository.save(stockEntity2);
    }

    @Test
    public void testGetStocks() throws Exception {
        mockMvc.perform(get("/stocks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.stocks[0].id").value(stockEntity1.getId()))
                .andExpect(jsonPath("$.stocks[0].name").value(stockEntity1.getName()))
                .andExpect(jsonPath("$.stocks[0].stockItems.length()").value(2))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].id").value(stockEntity1.getStockItems().get(0).getId()))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].quantity").value(stockEntity1.getStockItems().get(0).getQuantity()))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].price").value(stockEntity1.getStockItems().get(0).getPrice()))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].product.id").value(stockEntity1.getStockItems().get(0).getProduct().getId()))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].product.name").value(stockEntity1.getStockItems().get(0).getProduct().getName()))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].product.description").value(stockEntity1.getStockItems().get(0).getProduct().getDescription()))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].product.imagePath").value("/products/" + stockEntity1.getStockItems().get(0).getProduct().getId() + "/image"))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].id").value(stockEntity1.getStockItems().get(1).getId()))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].quantity").value(stockEntity1.getStockItems().get(1).getQuantity()))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].price").value(stockEntity1.getStockItems().get(1).getPrice()))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].product.id").value(stockEntity1.getStockItems().get(1).getProduct().getId()))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].product.name").value(stockEntity1.getStockItems().get(1).getProduct().getName()))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].product.description").value(stockEntity1.getStockItems().get(1).getProduct().getDescription()))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].product.imagePath").value("/products/" + stockEntity1.getStockItems().get(1).getProduct().getId() + "/image"))
                .andExpect(jsonPath("$.stocks[1].id").value(stockEntity2.getId()))
                .andExpect(jsonPath("$.stocks[1].name").value(stockEntity2.getName()))
                .andExpect(jsonPath("$.stocks[1].stockItems.length()").value(1))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].id").value(stockEntity2.getStockItems().get(0).getId()))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].quantity").value(stockEntity2.getStockItems().get(0).getQuantity()))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].price").value(stockEntity2.getStockItems().get(0).getPrice()))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].product.id").value(stockEntity2.getStockItems().get(0).getProduct().getId()))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].product.name").value(stockEntity2.getStockItems().get(0).getProduct().getName()))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].product.description").value(stockEntity2.getStockItems().get(0).getProduct().getDescription()))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].product.imagePath").value("/products/" + stockEntity2.getStockItems().get(0).getProduct().getId() + "/image"));
    }

    @Test
    public void testGetStockById() throws Exception {
        mockMvc.perform(get("/stocks/" + stockEntity1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(stockEntity1.getId()))
                .andExpect(jsonPath("$.name").value(stockEntity1.getName()))
                .andExpect(jsonPath("$.stockItems.length()").value(2))
                .andExpect(jsonPath("$.stockItems[0].id").value(stockEntity1.getStockItems().get(0).getId()))
                .andExpect(jsonPath("$.stockItems[0].quantity").value(stockEntity1.getStockItems().get(0).getQuantity()))
                .andExpect(jsonPath("$.stockItems[0].price").value(stockEntity1.getStockItems().get(0).getPrice()))
                .andExpect(jsonPath("$.stockItems[0].product.id").value(stockEntity1.getStockItems().get(0).getProduct().getId()))
                .andExpect(jsonPath("$.stockItems[0].product.name").value(stockEntity1.getStockItems().get(0).getProduct().getName()))
                .andExpect(jsonPath("$.stockItems[0].product.description").value(stockEntity1.getStockItems().get(0).getProduct().getDescription()))
                .andExpect(jsonPath("$.stockItems[0].product.imagePath").value("/products/" + stockEntity1.getStockItems().get(0).getProduct().getId() + "/image"))
                .andExpect(jsonPath("$.stockItems[1].id").value(stockEntity1.getStockItems().get(1).getId()))
                .andExpect(jsonPath("$.stockItems[1].quantity").value(stockEntity1.getStockItems().get(1).getQuantity()))
                .andExpect(jsonPath("$.stockItems[1].price").value(stockEntity1.getStockItems().get(1).getPrice()))
                .andExpect(jsonPath("$.stockItems[1].product.id").value(stockEntity1.getStockItems().get(1).getProduct().getId()))
                .andExpect(jsonPath("$.stockItems[1].product.name").value(stockEntity1.getStockItems().get(1).getProduct().getName()))
                .andExpect(jsonPath("$.stockItems[1].product.description").value(stockEntity1.getStockItems().get(1).getProduct().getDescription()))
                .andExpect(jsonPath("$.stockItems[1].product.imagePath").value("/products/" + stockEntity1.getStockItems().get(1).getProduct().getId() + "/image"));
    }

    @Test
    public void testGetStockByIdNotFound() throws Exception {
        mockMvc.perform(get("/stocks/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws  Exception {
        StockCreateRequest request = new StockCreateRequest("New Stock");

        assertEquals(2, stocksRepository.count());

        mockMvc.perform(post("/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        assertEquals(3, stocksRepository.count());
        StockEntity stockEntity = stocksRepository.findAll().getLast();
        assertEquals(request.name(), stockEntity.getName());
    }

    @Test
    public void testCreate_withInvalidName() throws Exception {
        StockCreateRequest request = new StockCreateRequest("");

        assertEquals(2, stocksRepository.count());

        mockMvc.perform(post("/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        assertEquals(2, stocksRepository.count());
    }

    @Test
    public void testUpdate() throws Exception {
        Long id = stockEntity1.getId();
        StockCreateRequest request = new StockCreateRequest("Updated Stock");

        StockEntity stockEntity = stocksRepository.findById(id).orElseThrow();
        assertNotEquals(request.name(), stockEntity.getName());

        mockMvc.perform(put("/stocks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Estoque atualizado com sucesso"));

        stockEntity = stocksRepository.findById(id).orElseThrow();
        assertEquals(request.name(), stockEntity.getName());
    }

    @Test
    public void testUpdate_withInvalidName() throws Exception {
        Long id = stockEntity1.getId();
        StockCreateRequest request = new StockCreateRequest("");

        StockEntity stockEntity = stocksRepository.findById(id).orElseThrow();
        assertNotEquals(request.name(), stockEntity.getName());

        mockMvc.perform(put("/stocks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        stockEntity = stocksRepository.findById(id).orElseThrow();
        assertNotEquals(request.name(), stockEntity.getName());
    }

    @Test
    public void testDeleteById() throws Exception {
        Long id = stockEntity1.getId();

        assertEquals(2, stocksRepository.count());

        mockMvc.perform(delete("/stocks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk());

        assertEquals(2, stocksRepository.count());
        assertEquals(1, stocksRepository.countAllByDeletedFalse());
    }

    @Test
    public void testDeleteById_whenNotFound() throws Exception {
        Long id = stockEntity1.getId();

        assertEquals(2, stocksRepository.count());

        mockMvc.perform(delete("/stocks/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());

        assertEquals(2, stocksRepository.count());
        assertEquals(2, stocksRepository.countAllByDeletedFalse());
    }
}