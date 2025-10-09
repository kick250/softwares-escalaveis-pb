package infra.orders.repositories;

import application.orders.domain.Order;
import application.orders.exceptions.OrderNotFoundException;
import application.orders.exceptions.OrderOwnerNotFoundException;
import application.orders.repositories.AllOrders;
import infra.global.entities.OrderEntity;
import infra.global.repositories.OrdersRepository;
import infra.orders.mappers.OrdersMapper;
import org.springframework.stereotype.Component;

@Component
public class OrdersJpaRepository implements AllOrders {
    private final OrdersRepository repository;
    private final OrdersMapper mapper;

    public OrdersJpaRepository(OrdersRepository repository, OrdersMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Order getById(Long orderId) throws OrderNotFoundException {
        OrderEntity order = repository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        return mapper.toDomain(order);
    }

    @Override
    public void create(Order order) throws OrderOwnerNotFoundException {
        repository.save(mapper.toEntity(order, null));
    }

    @Override
    public void update(Order order) throws OrderNotFoundException {
        OrderEntity existingOrder = repository.findById(order.getId()).orElseThrow(OrderNotFoundException::new);

        OrderEntity updatedOrder = mapper.toEntity(order, existingOrder);
        repository.save(updatedOrder);
    }
}
