package com.erp.server.responses;

import com.erp.server.entities.Product;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String imagePath
) {
    public ProductResponse(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                getImagePath(product)
        );
    }

    private static String getImagePath(Product product) {
        return "/products/" + product.getId() + "/image";
    }
}
