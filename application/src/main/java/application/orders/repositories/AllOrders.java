package application.orders.repositories;

import application.orders.domain.Order;
import application.orders.exceptions.OrderNotFoundException;
import application.orders.exceptions.OrderOwnerNotFoundException;

public interface AllOrders {

    void create(Order order) throws OrderOwnerNotFoundException;

    Order getById(Long orderId) throws OrderNotFoundException;

    void update(Order order) throws OrderNotFoundException;
}
