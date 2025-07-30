package com.erp.server.responses;

import com.erp.server.entities.Stock;

import java.util.List;

public record StockResponse(
        Long id,
        String name,
        List<StockItemResponse> stockItems
) {
    public StockResponse(Stock stock) {
        this(
                stock.getId(),
                stock.getName(),
                stock.getStockItems().stream().map(StockItemResponse::new).toList()
        );
    }
}
