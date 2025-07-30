package com.erp.server.responses;

import com.erp.server.entities.Product;

import java.util.List;

public record ProductsResponse(
        List<ProductResponse> products,
        int count
) {
    public ProductsResponse(List<Product> products) {
        this(products.stream().map(ProductResponse::new).toList(), products.size());
    }
}
