package com.erp.server.controllers;

import com.erp.server.entities.*;
import com.erp.server.factories.*;
import com.erp.server.repositories.*;
import com.erp.server.requests.StockItemCreateRequest;
import com.erp.server.requests.StockItemUpdateRequest;
import com.erp.server.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private Stock stock1;
    private Stock stock2;
    private StockItem stockItem1;
    private StockItem stockItem2;
    private StockItem stockItem3;
    private Product product1;
    private Product product2;

    @BeforeEach
    public void setUp() {
        usersRepository.deleteAll();
        stockItemsRepository.deleteAll();
        stocksRepository.deleteAll();
        productsRepository.deleteAll();
        attachmentsRepository.deleteAll();

        UsersFactory usersFactory = new UsersFactory();
        User user = usersFactory.createUser();
        usersRepository.save(user);

        token = "Bearer " + tokenService.generateToken(user);

        stock1 = stocksFactory.createStock();
        stock2 = stocksFactory.createStock();

        stocksRepository.save(stock1);
        stocksRepository.save(stock2);

        Attachment attachment1 = attachmentsFactory.createAttachment();
        attachmentsRepository.save(attachment1);
        Attachment attachment2 = attachmentsFactory.createAttachment();
        attachmentsRepository.save(attachment2);

        product1 = productsFactory.createProduct();
        product1.setAttachment(attachment1);
        productsRepository.save(product1);
        product2 = productsFactory.createProduct();
        product2.setAttachment(attachment2);
        productsRepository.save(product2);

        stockItem1 = stockItemsFactory.createStockItemWithProduct(product1);
        stockItemsRepository.save(stockItem1);
        stockItem2 = stockItemsFactory.createStockItemWithProduct(product2);
        stockItemsRepository.save(stockItem2);
        stockItem3 = stockItemsFactory.createStockItemWithProduct(product1);
        stockItemsRepository.save(stockItem3);

        stock1.addStockItem(stockItem1);
        stock1.addStockItem(stockItem2);
        stock2.addStockItem(stockItem3);

        stocksRepository.save(stock1);
        stocksRepository.save(stock2);
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
        StockItemCreateRequest request = new StockItemCreateRequest(100.0, 10, product2.getId(), stock2.getId());

        assertEquals(3, stockItemsRepository.count());

        mockMvc.perform(post("/stock_items")
                .header("Authorization", token)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("Item de estoque criado com sucesso."));

        assertEquals(4, stockItemsRepository.count());
    }

    @Test
    public void testCreate_whenStockAlreadyHasProduct() throws Exception {
        StockItemCreateRequest request = new StockItemCreateRequest(100.0, 10, product1.getId(), stock1.getId());

        assertEquals(3, stockItemsRepository.count());

        mockMvc.perform(post("/stock_items")
                .header("Authorization", token)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Esse estoque já possui um item com esse produto."));

        assertEquals(3, stockItemsRepository.count());
    }

    @Test
    public void testCreate_whenInvalidPrice() throws Exception {
        StockItemCreateRequest request = new StockItemCreateRequest(-100.0, 10, product2.getId(), stock2.getId());

        assertEquals(3, stockItemsRepository.count());

        mockMvc.perform(post("/stock_items")
                .header("Authorization", token)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        assertEquals(3, stockItemsRepository.count());
    }

    @Test
    public void testCreate_whenInvalidQuantity() throws Exception {
        StockItemCreateRequest request = new StockItemCreateRequest(100.0, -10, product2.getId(), stock2.getId());

        assertEquals(3, stockItemsRepository.count());

        mockMvc.perform(post("/stock_items")
                .header("Authorization", token)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        assertEquals(3, stockItemsRepository.count());
    }

    @Test
    public void testUpdate() throws Exception {
        StockItemUpdateRequest request = new StockItemUpdateRequest(150.0, 20);

        StockItem stockItem = stockItemsRepository.findById(stockItem1.getId()).orElseThrow();
        assertNotEquals(request.price(), stockItem.getPrice());
        assertNotEquals(request.quantity(), stockItem.getQuantity());

        mockMvc.perform(put("/stock_items/" + stockItem1.getId())
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        stockItem = stockItemsRepository.findById(stockItem1.getId()).orElseThrow();
        assertEquals(request.price(), stockItem.getPrice());
        assertEquals(request.quantity(), stockItem.getQuantity());
    }

    @Test
    public void testUpdate_whenInvalidPrice() throws Exception {
        StockItemUpdateRequest request = new StockItemUpdateRequest(-150.0, 20);

        StockItem stockItem = stockItemsRepository.findById(stockItem1.getId()).orElseThrow();
        assertNotEquals(request.price(), stockItem.getPrice());
        assertNotEquals(request.quantity(), stockItem.getQuantity());

        mockMvc.perform(put("/stock_items/" + stockItem1.getId())
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        stockItem = stockItemsRepository.findById(stockItem1.getId()).orElseThrow();
        assertNotEquals(request.price(), stockItem.getPrice());
        assertNotEquals(request.quantity(), stockItem.getQuantity());
    }

    @Test
    public void testUpdate_whenInvalidQuantity() throws Exception {
        StockItemUpdateRequest request = new StockItemUpdateRequest(150.0, -20);

        StockItem stockItem = stockItemsRepository.findById(stockItem1.getId()).orElseThrow();
        assertNotEquals(request.price(), stockItem.getPrice());
        assertNotEquals(request.quantity(), stockItem.getQuantity());

        mockMvc.perform(put("/stock_items/" + stockItem1.getId())
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        stockItem = stockItemsRepository.findById(stockItem1.getId()).orElseThrow();
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
        assertEquals(3, stockItemsRepository.count());

        mockMvc.perform(delete("/stock_items/" + stockItem1.getId())
                .header("Authorization", token))
                .andExpect(status().isOk());

        assertEquals(3, stockItemsRepository.count());
        assertEquals(2, stockItemsRepository.countAllByDeletedFalse());
    }

    @Test
    public void testDelete_whenNotFound() throws Exception {
        assertEquals(3, stockItemsRepository.count());

        mockMvc.perform(delete("/stock_items/9999")
                        .header("Authorization", token))
                .andExpect(status().isNotFound());

        assertEquals(3, stockItemsRepository.count());
        assertEquals(3, stockItemsRepository.countAllByDeletedFalse());
    }
}