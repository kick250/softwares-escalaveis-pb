package application.orders.domain;

import java.time.Instant;

public class Order {
    private Long id;
    private Instant createdAt;

    public Order(Long id, Instant createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public Order() {
        this.id = null;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
