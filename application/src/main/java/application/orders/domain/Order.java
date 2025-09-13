package application.orders.domain;

import application.orders.enums.OrderStatus;
import application.orders.exceptions.UnavailableItemQuantityException;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

public class Order {
    @Getter
    private Long id;
    @Getter
    private OrderStatus status;
    private User owner;
    @Getter
    private Instant createdAt;
    @Getter
    private Long stockId;
    private OrderItems items;
    private boolean stockUpdated = false;

    public Order(Long id, User owner, Instant createdAt, Long stockId, OrderItems items) {
        this.id = id;
        this.owner = owner;
        this.createdAt = createdAt;
        this.stockId = stockId;
        this.items = items;
        this.stockUpdated = true;
    }

    public Order(User owner, Long StockId, OrderItems items) {
        this.id = null;
        this.status = owner.exceedQuota(items) ? OrderStatus.WAITING_APPROVAL : OrderStatus.APPROVED;
        this.owner = owner;
        this.stockId = StockId;
        this.createdAt = Instant.now();
        this.items = items;
    }

    public Long getOwnerId() {
        return owner.getId();
    }

    public List<OrderItem> getOrderItems() {
        return items.getItems();
    }

    public List<Item> getItems() {
        return items.getItems().stream().map(OrderItem::getItem).toList();
    }

    public void updateStocks() throws UnavailableItemQuantityException {
        if (stockUpdated) return;

        items.decreaseItemsQuantity();
        stockUpdated = true;
    }
}
