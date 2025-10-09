package com.erp.server.responses.portal;

import infra.global.entities.StockItemEntity;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price,
        Integer quantityAvailable
) {
    public ProductResponse(StockItemEntity stockItem) {
        this(
                stockItem.getId(),
                stockItem.getName(),
                stockItem.getDescription(),
                stockItem.getPrice(),
                stockItem.getQuantity()
        );
    }
}
