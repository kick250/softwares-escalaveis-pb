package application.orders.domain;

import java.util.List;

public class OrderItems {
    List<OrderItem> items;

    public OrderItems(List<OrderItem> items) {
        this.items = items;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double totalAmount() {
        return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }

    public boolean exceedUserQuota(User owner) {
        return owner.exceedQuota(totalAmount());
    }
}
