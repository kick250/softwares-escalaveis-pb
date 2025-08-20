package com.erp.server.controllers;

import com.erp.server.entities.*;
import com.erp.server.factories.*;
import com.erp.server.repositories.*;
import com.erp.server.requests.StockCreateRequest;
import com.erp.server.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private Stock stock1;
    private Stock stock2;


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

        Product product1 = productsFactory.createProduct();
        product1.setAttachment(attachment1);
        productsRepository.save(product1);
        Product product2 = productsFactory.createProduct();
        product2.setAttachment(attachment2);
        productsRepository.save(product2);

        StockItem stockItem1 = stockItemsFactory.createStockItemWithProduct(product1);
        stockItemsRepository.save(stockItem1);
        StockItem stockItem2 = stockItemsFactory.createStockItemWithProduct(product2);
        stockItemsRepository.save(stockItem2);
        StockItem stockItem3 = stockItemsFactory.createStockItemWithProduct(product1);
        stockItemsRepository.save(stockItem3);

        stock1.addStockItem(stockItem1);
        stock1.addStockItem(stockItem2);
        stock2.addStockItem(stockItem3);

        stocksRepository.save(stock1);
        stocksRepository.save(stock2);
    }

    @Test
    public void testGetStocks() throws Exception {
        mockMvc.perform(get("/stocks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.stocks[0].id").value(stock1.getId()))
                .andExpect(jsonPath("$.stocks[0].name").value(stock1.getName()))
                .andExpect(jsonPath("$.stocks[0].stockItems.length()").value(2))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].id").value(stock1.getStockItems().get(0).getId()))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].quantity").value(stock1.getStockItems().get(0).getQuantity()))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].price").value(stock1.getStockItems().get(0).getPrice()))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].product.id").value(stock1.getStockItems().get(0).getProduct().getId()))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].product.name").value(stock1.getStockItems().get(0).getProduct().getName()))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].product.description").value(stock1.getStockItems().get(0).getProduct().getDescription()))
                .andExpect(jsonPath("$.stocks[0].stockItems[0].product.imagePath").value("/products/" + stock1.getStockItems().get(0).getProduct().getId() + "/image"))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].id").value(stock1.getStockItems().get(1).getId()))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].quantity").value(stock1.getStockItems().get(1).getQuantity()))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].price").value(stock1.getStockItems().get(1).getPrice()))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].product.id").value(stock1.getStockItems().get(1).getProduct().getId()))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].product.name").value(stock1.getStockItems().get(1).getProduct().getName()))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].product.description").value(stock1.getStockItems().get(1).getProduct().getDescription()))
                .andExpect(jsonPath("$.stocks[0].stockItems[1].product.imagePath").value("/products/" + stock1.getStockItems().get(1).getProduct().getId() + "/image"))
                .andExpect(jsonPath("$.stocks[1].id").value(stock2.getId()))
                .andExpect(jsonPath("$.stocks[1].name").value(stock2.getName()))
                .andExpect(jsonPath("$.stocks[1].stockItems.length()").value(1))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].id").value(stock2.getStockItems().get(0).getId()))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].quantity").value(stock2.getStockItems().get(0).getQuantity()))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].price").value(stock2.getStockItems().get(0).getPrice()))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].product.id").value(stock2.getStockItems().get(0).getProduct().getId()))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].product.name").value(stock2.getStockItems().get(0).getProduct().getName()))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].product.description").value(stock2.getStockItems().get(0).getProduct().getDescription()))
                .andExpect(jsonPath("$.stocks[1].stockItems[0].product.imagePath").value("/products/" + stock2.getStockItems().get(0).getProduct().getId() + "/image"));
    }

    @Test
    public void testGetStockById() throws Exception {
        mockMvc.perform(get("/stocks/" + stock1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(stock1.getId()))
                .andExpect(jsonPath("$.name").value(stock1.getName()))
                .andExpect(jsonPath("$.stockItems.length()").value(2))
                .andExpect(jsonPath("$.stockItems[0].id").value(stock1.getStockItems().get(0).getId()))
                .andExpect(jsonPath("$.stockItems[0].quantity").value(stock1.getStockItems().get(0).getQuantity()))
                .andExpect(jsonPath("$.stockItems[0].price").value(stock1.getStockItems().get(0).getPrice()))
                .andExpect(jsonPath("$.stockItems[0].product.id").value(stock1.getStockItems().get(0).getProduct().getId()))
                .andExpect(jsonPath("$.stockItems[0].product.name").value(stock1.getStockItems().get(0).getProduct().getName()))
                .andExpect(jsonPath("$.stockItems[0].product.description").value(stock1.getStockItems().get(0).getProduct().getDescription()))
                .andExpect(jsonPath("$.stockItems[0].product.imagePath").value("/products/" + stock1.getStockItems().get(0).getProduct().getId() + "/image"))
                .andExpect(jsonPath("$.stockItems[1].id").value(stock1.getStockItems().get(1).getId()))
                .andExpect(jsonPath("$.stockItems[1].quantity").value(stock1.getStockItems().get(1).getQuantity()))
                .andExpect(jsonPath("$.stockItems[1].price").value(stock1.getStockItems().get(1).getPrice()))
                .andExpect(jsonPath("$.stockItems[1].product.id").value(stock1.getStockItems().get(1).getProduct().getId()))
                .andExpect(jsonPath("$.stockItems[1].product.name").value(stock1.getStockItems().get(1).getProduct().getName()))
                .andExpect(jsonPath("$.stockItems[1].product.description").value(stock1.getStockItems().get(1).getProduct().getDescription()))
                .andExpect(jsonPath("$.stockItems[1].product.imagePath").value("/products/" + stock1.getStockItems().get(1).getProduct().getId() + "/image"));
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
        Stock stock = stocksRepository.findAll().getLast();
        assertEquals(request.name(), stock.getName());
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
        Long id = stock1.getId();
        StockCreateRequest request = new StockCreateRequest("Updated Stock");

        Stock stock = stocksRepository.findById(id).orElseThrow();
        assertNotEquals(request.name(), stock.getName());

        mockMvc.perform(put("/stocks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Estoque atualizado com sucesso"));

        stock = stocksRepository.findById(id).orElseThrow();
        assertEquals(request.name(), stock.getName());
    }

    @Test
    public void testUpdate_withInvalidName() throws Exception {
        Long id = stock1.getId();
        StockCreateRequest request = new StockCreateRequest("");

        Stock stock = stocksRepository.findById(id).orElseThrow();
        assertNotEquals(request.name(), stock.getName());

        mockMvc.perform(put("/stocks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        stock = stocksRepository.findById(id).orElseThrow();
        assertNotEquals(request.name(), stock.getName());
    }

    @Test
    public void testDeleteById() throws Exception {
        Long id = stock1.getId();

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
        Long id = stock1.getId();

        assertEquals(2, stocksRepository.count());

        mockMvc.perform(delete("/stocks/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isNotFound());

        assertEquals(2, stocksRepository.count());
        assertEquals(2, stocksRepository.countAllByDeletedFalse());
    }
}