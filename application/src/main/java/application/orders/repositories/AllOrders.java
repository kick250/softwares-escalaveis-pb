package application.orders.repositories;

import application.orders.domain.Order;
import application.orders.exceptions.OrderNotFoundException;

public interface AllOrders {

    public void update(Order order) throws OrderNotFoundException;

    public void create(Order order);
}
