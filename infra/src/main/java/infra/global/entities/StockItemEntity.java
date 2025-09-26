package infra.global.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="StockItem")
@Table(name="stock_items")
@AllArgsConstructor
@NoArgsConstructor
public class StockItemEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @Getter
    private boolean deleted = false;
    @Getter
    @Setter
    private double price;
    @Getter
    @Setter
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @Getter
    @Setter
    private ProductEntity product;
    @ManyToOne
    @JoinColumn(name = "stock_id")
    private StockEntity stock;

    public StockItemEntity(double price, int quantity, ProductEntity product, StockEntity stock) {
        this.price = price;
        this.quantity = quantity;
        this.product = product;
        this.stock = stock;
    }

    public Long getProductId() {
        return product.getId();
    }

    public String getName() {
        return product.getName();
    }

    public String getDescription() {
        return product.getDescription();
    }

    public void delete() {
        this.deleted = true;
    }

    public long getStockId() {
        return stock.getId();
    }
}
