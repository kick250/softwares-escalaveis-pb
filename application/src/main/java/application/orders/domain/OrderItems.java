package application.orders.domain;

import application.orders.exceptions.UnavailableItemQuantityException;
import lombok.Getter;

import java.util.List;

public class OrderItems {
    @Getter
    List<OrderItem> items;

    public OrderItems(List<OrderItem> items) {
        this.items = items;
    }

    public double totalAmount() {
        return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }

    public void decreaseItemsQuantity() throws UnavailableItemQuantityException {
        for (OrderItem item : items)
            item.decreaseItemQuantity();
    }
}
