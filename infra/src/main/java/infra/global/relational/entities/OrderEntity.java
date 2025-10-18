package infra.global.relational.entities;

import application.orders.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Entity(name="Order")
@Table(name="orders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    private boolean deleted = false;
    @Column(columnDefinition="jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    @Setter
    private OrderStatus status;
    @Getter
    @Setter
    private Instant createdAt;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @Setter
    private UserEntity owner;
    @ManyToOne
    @JoinColumn(name = "stock_id")
    @Setter
    private StockEntity stock;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @Setter
    @Getter
    private List<OrderItemEntity> items;

    public Long getStockId() {
        return stock.getId();
    }

    public Optional<OrderItemEntity> getItemById(Long itemId) {
        return items.stream().filter(item -> item.getId().equals(itemId)).findFirst();
    }

    public String getStockName() {
        return stock.getName();
    }

    public String getOwnerName() {
        return owner.getName();
    }
}
