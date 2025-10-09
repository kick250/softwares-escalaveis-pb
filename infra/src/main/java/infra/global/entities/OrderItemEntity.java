package infra.global.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity(name="OrderItem")
@Table(name="order_items")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    private boolean deleted = false;
    @Setter
    private int quantity;
    @Setter
    private double price;
    @ManyToOne
    @JoinColumn(name = "order_id")
    @Setter
    private OrderEntity order;
    @ManyToOne
    @JoinColumn(name = "stock_item_id")
    @Setter
    @Getter
    private StockItemEntity stockItem;

    public String getName() {
        return stockItem.getName();
    }

    public Double getTotalPrice() {
        return this.price * this.quantity;
    }
}
