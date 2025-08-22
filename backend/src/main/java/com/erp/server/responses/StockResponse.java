package com.erp.server.responses;

import infra.global.entities.StockEntity;

import java.util.List;

public record StockResponse(
        Long id,
        String name,
        List<StockItemResponse> stockItems
) {
    public StockResponse(StockEntity stockEntity) {
        this(
                stockEntity.getId(),
                stockEntity.getName(),
                stockEntity.getStockItems().stream().map(StockItemResponse::new).toList()
        );
    }
}
