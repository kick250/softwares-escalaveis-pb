package com.erp.server.controllers.portal;

import application.orders.enums.OrderStatus;
import com.erp.server.factories.*;
import com.erp.server.requests.OrderCreateRequest;
import com.erp.server.requests.OrderItemRequest;
import com.erp.server.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import infra.global.entities.*;
import infra.global.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrdersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UsersFactory usersFactory = new UsersFactory();
    private final StocksFactory stocksFactory = new StocksFactory();
    private final StockItemsFactory stockItemsFactory = new StockItemsFactory();
    private final ProductsFactory productsFactory = new ProductsFactory();
    private final AttachmentsFactory attachmentsFactory = new AttachmentsFactory();

    private UserEntity currentUser;
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
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemsRepository ordersItemsRepository;
    @Autowired
    private TokenService tokenService;

    private StockEntity stock1;
    private StockItemEntity stockItem1;
    private StockItemEntity stockItem2;
    private ProductEntity product1;
    private ProductEntity product2;

    @BeforeEach
    public void setUp() {
        ordersItemsRepository.deleteAll();
        ordersRepository.deleteAll();
        usersRepository.deleteAll();
        stockItemsRepository.deleteAll();
        stocksRepository.deleteAll();
        productsRepository.deleteAll();
        attachmentsRepository.deleteAll();

        currentUser = usersFactory.createPortalUser();
        usersRepository.save(currentUser);

        token = "Bearer " + tokenService.generateToken(currentUser);

        stock1 = stocksFactory.createStock();

        stocksRepository.save(stock1);

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
    }

    @Test
    public void testCreate() throws Exception {
        List<OrderItemRequest> itemsRequest = new ArrayList<>();
        itemsRequest.add(new OrderItemRequest(stockItem1.getId(), 10));
        itemsRequest.add(new OrderItemRequest(stockItem2.getId(), 5));
        OrderCreateRequest body = new OrderCreateRequest(stock1.getId(), itemsRequest);

        assertEquals(0, ordersRepository.count());

        mockMvc.perform(post("/portal/orders")
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());

        assertEquals(1, ordersRepository.count());
        OrderEntity order = ordersRepository.findAll().getLast();
        order = ordersRepository.findByIdWithItems(order.getId()).get();
        assertNotNull(order);
        assertEquals(OrderStatus.APPROVED, order.getStatus());
        assertNotNull(order.getCreatedAt());
        assertEquals(currentUser, order.getOwner());
        assertEquals(2, order.getItems().size());
        OrderItemEntity orderItem1 = order.getItems().stream().filter(i -> i.getStockItem().getId().equals(stockItem1.getId())).findFirst().get();
        assertEquals(10, orderItem1.getQuantity());
        assertEquals(stockItem1.getPrice(), orderItem1.getPrice());
        OrderItemEntity orderItem2 = order.getItems().stream().filter(i -> i.getStockItem().getId().equals(stockItem2.getId())).findFirst().get();
        assertEquals(5, orderItem2.getQuantity());
        assertEquals(stockItem2.getPrice(), orderItem2.getPrice());
    }

    @Test
    public void testCreate_whenExceedsQuota() throws Exception {
        List<OrderItemRequest> itemsRequest = new ArrayList<>();
        itemsRequest.add(new OrderItemRequest(stockItem1.getId(), 100));
        itemsRequest.add(new OrderItemRequest(stockItem2.getId(), 100));
        OrderCreateRequest body = new OrderCreateRequest(stock1.getId(), itemsRequest);

        assertEquals(0, ordersRepository.count());

        mockMvc.perform(post("/portal/orders")
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());

        assertEquals(1, ordersRepository.count());
        OrderEntity order = ordersRepository.findAll().getLast();
        order = ordersRepository.findByIdWithItems(order.getId()).get();
        assertNotNull(order);
        assertEquals(OrderStatus.WAITING_APPROVAL, order.getStatus());
        assertNotNull(order.getCreatedAt());
        assertEquals(currentUser, order.getOwner());
        assertEquals(2, order.getItems().size());
        OrderItemEntity orderItem1 = order.getItems().stream().filter(i -> i.getStockItem().getId().equals(stockItem1.getId())).findFirst().get();
        assertEquals(100, orderItem1.getQuantity());
        assertEquals(stockItem1.getPrice(), orderItem1.getPrice());
        OrderItemEntity orderItem2 = order.getItems().stream().filter(i -> i.getStockItem().getId().equals(stockItem2.getId())).findFirst().get();
        assertEquals(100, orderItem2.getQuantity());
        assertEquals(stockItem2.getPrice(), orderItem2.getPrice());
    }
}