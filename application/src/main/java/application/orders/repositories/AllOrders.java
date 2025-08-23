package application.orders.repositories;

import application.orders.domain.Order;
import application.orders.exceptions.OrderNotFoundException;
import application.orders.exceptions.OrderOwnerNotFoundException;

public interface AllOrders {

    public void create(Order order) throws OrderOwnerNotFoundException;
}
