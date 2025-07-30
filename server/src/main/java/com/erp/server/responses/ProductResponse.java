package com.erp.server.responses;

import com.erp.server.entities.Product;

public record ProductResponse(
        Long id,
        String name,
        String description
) {
    public ProductResponse(Product product) {
        this(product.getId(), product.getName(), product.getDescription());
    }
}
