package application.orders.domain;

import application.orders.exceptions.UnavailableItemQuantityException;
import lombok.Getter;

public class OrderItem {
    @Getter
    private Long id;
    @Getter
    private Item item;
    @Getter
    private int quantity;
    @Getter
    private double price;

    public OrderItem(Long id, Item item, int quantity, double price) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getItemId() {
        return item.getId();
    }

    public void decreaseItemQuantity() throws UnavailableItemQuantityException {
        item.decreaseAvailableQuantity(quantity);
    }
}
