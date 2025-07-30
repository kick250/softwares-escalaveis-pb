package com.erp.server.responses;

import com.erp.server.entities.StockItem;

public record StockItemResponse(
        Long id,
        String name,
        String description,
        Integer quantity,
        Double price,
        Long productId
) {
    public StockItemResponse(StockItem stockItem) {
        this(
                stockItem.getId(),
                stockItem.getName(),
                stockItem.getDescription(),
                stockItem.getQuantity(),
                stockItem.getPrice(),
                stockItem.getProductId()
        );
    }
}
