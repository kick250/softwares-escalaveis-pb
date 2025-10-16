package com.erp.server.controllers.portal;

import application.orders.enums.OrderStatus;
import com.erp.server.factories.*;
import com.erp.server.requests.OrderCreateRequest;
import com.erp.server.requests.OrderItemRequest;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private UserEntity adminUser;
    private String adminToken;

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
    private OrderEntity preCreatedOrder;

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

        adminUser = usersFactory.createAdminUser();
        usersRepository.save(adminUser);
        adminToken = "Bearer " + tokenService.generateToken(adminUser);

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

        preCreatedOrder = new OrderEntity();
        preCreatedOrder.setOwner(currentUser);
        preCreatedOrder.setStatus(OrderStatus.APPROVED);
        preCreatedOrder.setStock(stock1);
        preCreatedOrder.setCreatedAt(Instant.now());
        preCreatedOrder.setItems(List.of(
                new OrderItemEntity(null, false, 5, stockItem1.getPrice(), preCreatedOrder, stockItem1),
                new OrderItemEntity(null, false, 2, stockItem2.getPrice(), preCreatedOrder, stockItem2)
        ));
        ordersRepository.save(preCreatedOrder);
    }

    @AfterEach
    public void tearDown() {
        ordersItemsRepository.deleteAll();
        ordersRepository.deleteAll();
        usersRepository.deleteAll();
        stockItemsRepository.deleteAll();
        stocksRepository.deleteAll();
        productsRepository.deleteAll();
        attachmentsRepository.deleteAll();
    }

    @Test
    public void testGetAll() throws Exception {
        preCreatedOrder = ordersRepository.findByIdWithItems(preCreatedOrder.getId()).get();

        mockMvc.perform(get("/portal/orders")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(preCreatedOrder.getId()))
                .andExpect(jsonPath("$[0].status").value(preCreatedOrder.getStatus().toString()))
                .andExpect(jsonPath("$[0].stockName").value(preCreatedOrder.getStockName()))
                .andExpect(jsonPath("$[0].ownerName").value(preCreatedOrder.getOwnerName()))
                .andExpect(jsonPath("$[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$[0].items.length()").value(2))
                .andExpect(jsonPath("$[0].items[0].id").value(preCreatedOrder.getItems().get(0).getId()))
                .andExpect(jsonPath("$[0].items[0].name").value(preCreatedOrder.getItems().get(0).getName()))
                .andExpect(jsonPath("$[0].items[0].quantity").value(5))
                .andExpect(jsonPath("$[0].items[0].pricePerUnit").value(preCreatedOrder.getItems().get(0).getPrice()))
                .andExpect(jsonPath("$[0].items[0].totalPrice").value(preCreatedOrder.getItems().get(0).getTotalPrice()))
                .andExpect(jsonPath("$[0].items[1].id").value(preCreatedOrder.getItems().get(1).getId()))
                .andExpect(jsonPath("$[0].items[1].name").value(preCreatedOrder.getItems().get(1).getName()))
                .andExpect(jsonPath("$[0].items[1].quantity").value(2))
                .andExpect(jsonPath("$[0].items[1].pricePerUnit").value(preCreatedOrder.getItems().get(1).getPrice()))
                .andExpect(jsonPath("$[0].items[1].totalPrice").value(preCreatedOrder.getItems().get(1).getTotalPrice()));
    }

    @Test
    public void testGetById() throws Exception {
        preCreatedOrder = ordersRepository.findByIdWithItems(preCreatedOrder.getId()).get();

        mockMvc.perform(get("/portal/orders/" + preCreatedOrder.getId())
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(preCreatedOrder.getId()))
                .andExpect(jsonPath("$.status").value(preCreatedOrder.getStatus().toString()))
                .andExpect(jsonPath("$.stockName").value(preCreatedOrder.getStockName()))
                .andExpect(jsonPath("$.ownerName").value(preCreatedOrder.getOwnerName()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].id").value(preCreatedOrder.getItems().get(0).getId()))
                .andExpect(jsonPath("$.items[0].name").value(preCreatedOrder.getItems().get(0).getName()))
                .andExpect(jsonPath("$.items[0].quantity").value(5))
                .andExpect(jsonPath("$.items[0].pricePerUnit").value(preCreatedOrder.getItems().get(0).getPrice()))
                .andExpect(jsonPath("$.items[0].totalPrice").value(preCreatedOrder.getItems().get(0).getTotalPrice()))
                .andExpect(jsonPath("$.items[1].id").value(preCreatedOrder.getItems().get(1).getId()))
                .andExpect(jsonPath("$.items[1].name").value(preCreatedOrder.getItems().get(1).getName()))
                .andExpect(jsonPath("$.items[1].quantity").value(2))
                .andExpect(jsonPath("$.items[1].pricePerUnit").value(preCreatedOrder.getItems().get(1).getPrice()))
                .andExpect(jsonPath("$.items[1].totalPrice").value(preCreatedOrder.getItems().get(1).getTotalPrice()));
    }

    @Test
    public void testGetById_whenNotFound() throws Exception {
        mockMvc.perform(get("/portal/orders/9999")
                        .header("Authorization", token))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {
        List<OrderItemRequest> itemsRequest = new ArrayList<>();
        itemsRequest.add(new OrderItemRequest(stockItem1.getId(), 10));
        itemsRequest.add(new OrderItemRequest(stockItem2.getId(), 5));
        OrderCreateRequest body = new OrderCreateRequest(stock1.getId(), itemsRequest);

        assertEquals(1, ordersRepository.count());

        mockMvc.perform(post("/portal/orders")
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());

        assertEquals(2, ordersRepository.count());
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
        assertEquals(90, orderItem1.getStockItem().getQuantity());
        OrderItemEntity orderItem2 = order.getItems().stream().filter(i -> i.getStockItem().getId().equals(stockItem2.getId())).findFirst().get();
        assertEquals(5, orderItem2.getQuantity());
        assertEquals(stockItem2.getPrice(), orderItem2.getPrice());
        assertEquals(95, orderItem2.getStockItem().getQuantity());
    }

    @Test
    public void testCreate_whenExceedsQuota() throws Exception {
        List<OrderItemRequest> itemsRequest = new ArrayList<>();
        itemsRequest.add(new OrderItemRequest(stockItem1.getId(), 60));
        itemsRequest.add(new OrderItemRequest(stockItem2.getId(), 60));
        OrderCreateRequest body = new OrderCreateRequest(stock1.getId(), itemsRequest);

        assertEquals(1, ordersRepository.count());

        mockMvc.perform(post("/portal/orders")
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());

        assertEquals(2, ordersRepository.count());
        OrderEntity order = ordersRepository.findAll().getLast();
        order = ordersRepository.findByIdWithItems(order.getId()).get();
        assertNotNull(order);
        assertEquals(OrderStatus.WAITING_APPROVAL, order.getStatus());
        assertNotNull(order.getCreatedAt());
        assertEquals(currentUser, order.getOwner());
        assertEquals(2, order.getItems().size());
        OrderItemEntity orderItem1 = order.getItems().stream().filter(i -> i.getStockItem().getId().equals(stockItem1.getId())).findFirst().get();
        assertEquals(60, orderItem1.getQuantity());
        assertEquals(stockItem1.getPrice(), orderItem1.getPrice());
        assertEquals(40, orderItem1.getStockItem().getQuantity());
        OrderItemEntity orderItem2 = order.getItems().stream().filter(i -> i.getStockItem().getId().equals(stockItem2.getId())).findFirst().get();
        assertEquals(60, orderItem2.getQuantity());
        assertEquals(stockItem2.getPrice(), orderItem2.getPrice());
        assertEquals(40, orderItem2.getStockItem().getQuantity());
    }

    @Test
    public void testCreate_whenExceedsQuota_whenIsAdmin() throws Exception {
        List<OrderItemRequest> itemsRequest = new ArrayList<>();
        itemsRequest.add(new OrderItemRequest(stockItem1.getId(), 60));
        itemsRequest.add(new OrderItemRequest(stockItem2.getId(), 60));
        OrderCreateRequest body = new OrderCreateRequest(stock1.getId(), itemsRequest);

        assertEquals(1, ordersRepository.count());

        mockMvc.perform(post("/portal/orders")
                        .header("Authorization", adminToken)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());

        assertEquals(2, ordersRepository.count());
        OrderEntity order = ordersRepository.findAll().getLast();
        order = ordersRepository.findByIdWithItems(order.getId()).get();
        assertNotNull(order);
        assertEquals(OrderStatus.APPROVED, order.getStatus());
        assertNotNull(order.getCreatedAt());
        assertEquals(adminUser, order.getOwner());
        assertEquals(2, order.getItems().size());
        OrderItemEntity orderItem1 = order.getItems().stream().filter(i -> i.getStockItem().getId().equals(stockItem1.getId())).findFirst().get();
        assertEquals(60, orderItem1.getQuantity());
        assertEquals(stockItem1.getPrice(), orderItem1.getPrice());
        assertEquals(40, orderItem1.getStockItem().getQuantity());
        OrderItemEntity orderItem2 = order.getItems().stream().filter(i -> i.getStockItem().getId().equals(stockItem2.getId())).findFirst().get();
        assertEquals(60, orderItem2.getQuantity());
        assertEquals(stockItem2.getPrice(), orderItem2.getPrice());
        assertEquals(40, orderItem2.getStockItem().getQuantity());
    }

    @Test
    public void testCreate_whenUnavailableItemQuantity() throws Exception {
        List<OrderItemRequest> itemsRequest = new ArrayList<>();
        itemsRequest.add(new OrderItemRequest(stockItem1.getId(), 1000));
        itemsRequest.add(new OrderItemRequest(stockItem2.getId(), 5));
        OrderCreateRequest body = new OrderCreateRequest(stock1.getId(), itemsRequest);

        assertEquals(1, ordersRepository.count());

        mockMvc.perform(post("/portal/orders")
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());

        assertEquals(1, ordersRepository.count());
    }

    @Test
    public void testCreate_whenSomeItemsWereNotFound() throws Exception {
        List<OrderItemRequest> itemsRequest = new ArrayList<>();
        itemsRequest.add(new OrderItemRequest(9999L, 10));
        itemsRequest.add(new OrderItemRequest(stockItem2.getId(), 5));
        OrderCreateRequest body = new OrderCreateRequest(stock1.getId(), itemsRequest);

        assertEquals(1, ordersRepository.count());

        mockMvc.perform(post("/portal/orders")
                        .header("Authorization", token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());

        assertEquals(1, ordersRepository.count());
    }

    @Test
    public void testApprove_whenUserIsAdmin() throws Exception {
        preCreatedOrder.setStatus(OrderStatus.WAITING_APPROVAL);
        ordersRepository.save(preCreatedOrder);

        assertEquals(1, ordersRepository.count());

        mockMvc.perform(post("/portal/orders/" + preCreatedOrder.getId() + "/approve")
                        .header("Authorization", adminToken))
                .andExpect(status().isCreated());

        assertEquals(1, ordersRepository.count());
        OrderEntity approvedOrder = ordersRepository.findByIdWithItems(preCreatedOrder.getId()).get();
        assertNotNull(approvedOrder);
        assertEquals(OrderStatus.APPROVED, approvedOrder.getStatus());
    }

    @Test
    public void testApprove_whenNotAdmin() throws Exception {
        preCreatedOrder.setStatus(OrderStatus.WAITING_APPROVAL);
        ordersRepository.save(preCreatedOrder);

        assertEquals(1, ordersRepository.count());

        mockMvc.perform(post("/portal/orders/" + preCreatedOrder.getId() + "/approve")
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());

        assertEquals(1, ordersRepository.count());
        OrderEntity approvedOrder = ordersRepository.findByIdWithItems(preCreatedOrder.getId()).get();
        assertNotNull(approvedOrder);
        assertEquals(OrderStatus.WAITING_APPROVAL, approvedOrder.getStatus());
    }

    @Test
    public void testApprove_whenNotFound() throws Exception {
        assertEquals(1, ordersRepository.count());

        mockMvc.perform(post("/portal/orders/9999/approve")
                        .header("Authorization", adminToken))
                .andExpect(status().isNotFound());

        assertEquals(1, ordersRepository.count());
    }

    @Test
    public void testCancel_whenAdmin() throws Exception {
        preCreatedOrder.setStatus(OrderStatus.WAITING_APPROVAL);
        ordersRepository.save(preCreatedOrder);

        assertEquals(1, ordersRepository.count());

        mockMvc.perform(post("/portal/orders/" + preCreatedOrder.getId() + "/cancel")
                        .header("Authorization", adminToken))
                .andExpect(status().isOk());

        assertEquals(1, ordersRepository.count());
        OrderEntity cancelledOrder = ordersRepository.findByIdWithItems(preCreatedOrder.getId()).get();
        assertNotNull(cancelledOrder);
        assertEquals(OrderStatus.CANCELED, cancelledOrder.getStatus());
    }

    @Test
    public void testCancel_whenNotAdmin() throws Exception {
        preCreatedOrder.setStatus(OrderStatus.WAITING_APPROVAL);
        ordersRepository.save(preCreatedOrder);

        assertEquals(1, ordersRepository.count());

        mockMvc.perform(post("/portal/orders/" + preCreatedOrder.getId() + "/cancel")
                        .header("Authorization", token))
                .andExpect(status().isBadRequest());

        assertEquals(1, ordersRepository.count());
        OrderEntity cancelledOrder = ordersRepository.findByIdWithItems(preCreatedOrder.getId()).get();
        assertNotNull(cancelledOrder);
        assertEquals(OrderStatus.WAITING_APPROVAL, cancelledOrder.getStatus());
    }

    @Test
    public void testCancel_whenNotFound() throws Exception {
        assertEquals(1, ordersRepository.count());

        mockMvc.perform(post("/portal/orders/9999/cancel")
                        .header("Authorization", adminToken))
                .andExpect(status().isNotFound());

        assertEquals(1, ordersRepository.count());
    }
}