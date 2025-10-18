package com.erp.server.responses;

import infra.global.relational.entities.ProductEntity;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String imagePath
) {
    public ProductResponse(ProductEntity productEntity) {
        this(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getDescription(),
                getImagePath(productEntity)
        );
    }

    private static String getImagePath(ProductEntity productEntity) {
        return "/products/" + productEntity.getId() + "/image";
    }
}
