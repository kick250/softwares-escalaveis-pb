package infra.orders.mappers;

import application.orders.domain.Order;
import application.orders.domain.OrderItem;
import infra.global.entities.*;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrdersMapper {
    private final EntityManager entityManager;

    public OrdersMapper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public OrderEntity toEntity(Order order) {
        UserEntity owner = entityManager.getReference(UserEntity.class, order.getOwnerId());
        StockEntity stock = entityManager.getReference(StockEntity.class, order.getStockId());

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(order.getId());
        orderEntity.setStatus(order.getStatus());
        orderEntity.setCreatedAt(order.getCreatedAt());
        orderEntity.setOwner(owner);
        orderEntity.setStock(stock);

        List<OrderItemEntity> items = order.getOrderItems().stream().map(item -> itemToEntity(orderEntity, item)).toList();
        orderEntity.setItems(items);

        return orderEntity;
    }

    public OrderItemEntity itemToEntity(OrderEntity orderEntity, OrderItem orderItem) {
        StockItemEntity stockItemEntity = entityManager.getReference(StockItemEntity.class, orderItem.getItemId());

        OrderItemEntity orderItemEntity = new OrderItemEntity();

        orderItemEntity.setId(orderItem.getId());
        orderItemEntity.setQuantity(orderItem.getQuantity());
        orderItemEntity.setPrice(orderItem.getPrice());
        orderItemEntity.setOrder(orderEntity);
        orderItemEntity.setStockItem(stockItemEntity);

        return orderItemEntity;
    }
}
