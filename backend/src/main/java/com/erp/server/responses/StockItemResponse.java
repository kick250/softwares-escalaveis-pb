package com.erp.server.responses;

import infra.global.relational.entities.StockItemEntity;

public record StockItemResponse(
        Long id,
        Integer quantity,
        Double price,
        ProductResponse product
) {
    public StockItemResponse(StockItemEntity stockItem) {
        this(
                stockItem.getId(),
                stockItem.getQuantity(),
                stockItem.getPrice(),
                new ProductResponse(stockItem.getProduct())
        );
    }
}
