package com.erp.server.responses;

import com.erp.server.entities.StockItem;

public record StockItemResponse(
        Long id,
        Integer quantity,
        Double price,
        ProductResponse product
) {
    public StockItemResponse(StockItem stockItem) {
        this(
                stockItem.getId(),
                stockItem.getQuantity(),
                stockItem.getPrice(),
                new ProductResponse(stockItem.getProduct())
        );
    }
}
