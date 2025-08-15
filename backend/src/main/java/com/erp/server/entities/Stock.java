package com.erp.server.entities;

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
public class Stock {
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
    private List<StockItem> stockItems;

    public Stock(String name) {
        this.name = name;
    }

    public void delete() {
        stockItems.forEach(StockItem::delete);
        this.deleted = true;
    }

    public boolean hasItemWithProduct(Product product) {
        return stockItems.stream().anyMatch(item -> item.getProduct().getId().equals(product.getId()) && !item.isDeleted());
    }
}
