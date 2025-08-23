package application.orders.domain;

import application.orders.enums.OrderStatus;

import java.time.Instant;
import java.util.List;

public class Order {
    private Long id;
    private OrderStatus status;
    private User owner;
    private Instant createdAt;
    private Long stockId;
    private OrderItems items;

    public Order(Long id, User owner, Instant createdAt, Long stockId, OrderItems items) {
        this.id = id;
        this.owner = owner;
        this.createdAt = createdAt;
        this.stockId = stockId;
        this.items = items;
    }

    public Order(User Owner, Long StockId, OrderItems items) {
        this.id = null;
        this.status = items.exceedUserQuota(Owner) ? OrderStatus.WAITING_APPROVAL : OrderStatus.APPROVED;
        this.owner = Owner;
        this.stockId = StockId;
        this.createdAt = Instant.now();
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Long getOwnerId() {
        return owner.getId();
    }

    public Long getStockId() {
        return stockId;
    }

    public List<OrderItem> getItems() {
        return items.getItems();
    }
}
