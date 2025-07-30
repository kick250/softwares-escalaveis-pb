package com.erp.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name="Product")
@Table(name="products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    private Long id;
    private boolean deleted = false;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String description;
    @OneToMany
    @JoinColumn(name = "product_id")
    private List<StockItem> stockItems;


    public Product(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void delete() {
        this.deleted = true;
    }
}
