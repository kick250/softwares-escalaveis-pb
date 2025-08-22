package application.orders.actions;

import application.orders.domain.Order;
import application.orders.repositories.AllOrders;

public class CreateOrder {
    private final AllOrders allOrders;

    public CreateOrder(AllOrders allOrders) {
        this.allOrders = allOrders;
    }

    public void execute() {
        Order order = new Order();
        allOrders.create(order);
    }
}
