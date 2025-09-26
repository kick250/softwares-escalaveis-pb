package application.orders.domain;

import application.orders.exceptions.UnavailableItemQuantityException;
import lombok.Getter;

public class Item {
    @Getter
    private Long id;
    @Getter
    private String name;
    @Getter
    private Long stockId;
    @Getter
    private int availableQuantity;
    @Getter
    private double price;

    public Item(Long id, String name, Long stockId, int availableQuantity, double price) {
        this.id = id;
        this.name = name;
        this.stockId = stockId;
        this.availableQuantity = availableQuantity;
        this.price = price;
    }

    public boolean isFromStock(Long stockId) {
        return this.stockId.equals(stockId);
    }

    public boolean hasAvailableQuantity(int quantity) {
        return this.availableQuantity >= quantity;
    }

    public void decreaseAvailableQuantity(int quantity) throws UnavailableItemQuantityException {
        if (!hasAvailableQuantity(quantity))
            throw new UnavailableItemQuantityException(this);

        this.availableQuantity -= quantity;
    }
}
