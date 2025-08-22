package infra.orders.mappers;

import application.orders.domain.Order;
import infra.global.entities.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrdersMapper {
    public OrderEntity toEntity(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(order.getId());
        orderEntity.setCreatedAt(order.getCreatedAt());
        return orderEntity;
    }

    public OrderEntity toEntity(OrderEntity orderEntity, Order orderDomain) {
        orderEntity.setId(orderDomain.getId());
        orderEntity.setCreatedAt(orderDomain.getCreatedAt());
        return orderEntity;
    }
}
