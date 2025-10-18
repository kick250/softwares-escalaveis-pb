package com.erp.server.responses;

import infra.global.relational.entities.ProductEntity;

import java.util.List;

public record ProductsResponse(
        List<ProductResponse> products,
        int count
) {
    public ProductsResponse(List<ProductEntity> productEntities) {
        this(productEntities.stream().map(ProductResponse::new).toList(), productEntities.size());
    }
}
