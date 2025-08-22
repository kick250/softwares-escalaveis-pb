package infra.global.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity(name="Stock")
@Table(name="stocks")
@AllArgsConstructor
@NoArgsConstructor
public class StockEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    private Long id;
    private boolean deleted = false;
    @Getter
    @Setter
    private String name;
    @OneToMany
    @JoinColumn(name = "stock_id")
    @SQLRestriction("deleted = false")
    @Getter
    private List<StockItemEntity> stockItems;

    public StockEntity(String name) {
        this.name = name;
    }

    public void delete() {
        stockItems.forEach(StockItemEntity::delete);
        this.deleted = true;
    }

    public boolean hasItemWithProduct(ProductEntity productEntity) {
        return stockItems.stream().anyMatch(item -> item.getProduct().getId().equals(productEntity.getId()) && !item.isDeleted());
    }

    public void addStockItem(StockItemEntity stockItem) {
        if (stockItem != null && !hasItemWithProduct(stockItem.getProduct())) {
            stockItems.add(stockItem);
        }
    }
}
