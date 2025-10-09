package infra.orders.mappers;

import application.orders.domain.*;
import infra.global.entities.*;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrdersMapper {
    private final EntityManager entityManager;

    public OrdersMapper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public OrderEntity toEntity(Order order, OrderEntity existingOrder) {
        UserEntity owner = entityManager.getReference(UserEntity.class, order.getOwnerId());
        StockEntity stock = entityManager.getReference(StockEntity.class, order.getStockId());

        OrderEntity orderEntity = existingOrder != null ? existingOrder : new OrderEntity();

        orderEntity.setStatus(order.getStatus());
        orderEntity.setCreatedAt(order.getCreatedAt());
        orderEntity.setOwner(owner);
        orderEntity.setStock(stock);

        if (orderEntity.getItems() == null)
            orderEntity.setItems(new ArrayList<>());

        List<OrderItemEntity> items = order.getOrderItems().stream()
                .map(item -> itemToEntity(orderEntity, item))
                .toList();

        orderEntity.getItems().addAll(items);

        return orderEntity;
    }

    public OrderItemEntity itemToEntity(OrderEntity orderEntity, OrderItem orderItem) {
        StockItemEntity stockItemEntity = entityManager.getReference(StockItemEntity.class, orderItem.getItemId());

        OrderItemEntity orderItemEntity;

        if (orderItem.getId() != null) {
            orderItemEntity = orderEntity.getItemById(orderItem.getId()).orElse(new OrderItemEntity());
        } else {
            orderItemEntity = new OrderItemEntity();
        }

        orderItemEntity.setQuantity(orderItem.getQuantity());
        orderItemEntity.setPrice(orderItem.getPrice());
        orderItemEntity.setStockItem(stockItemEntity);
        orderItemEntity.setOrder(orderEntity);

        return orderItemEntity;
    }

    public Order toDomain(OrderEntity order) {
        User owner = new User(order.getOwner().getId(), order.getOwner().isAdmin());
        List<OrderItem> items = order.getItems().stream().map(this::itemToDomain).toList();

        return new Order(
                order.getId(),
                order.getStatus(),
                owner,
                order.getCreatedAt(),
                order.getStockId(),
                new OrderItems(items)
        );
    }

    private OrderItem itemToDomain(OrderItemEntity orderItemEntity) {
        StockItemEntity stockItemEntity = orderItemEntity.getStockItem();
        Item item = new Item(
                stockItemEntity.getId(),
                stockItemEntity.getName(),
                stockItemEntity.getStockId(),
                stockItemEntity.getQuantity(),
                stockItemEntity.getPrice()
        );

        return new OrderItem(
                orderItemEntity.getId(),
                item,
                orderItemEntity.getQuantity(),
                orderItemEntity.getPrice()
        );
    }
}
