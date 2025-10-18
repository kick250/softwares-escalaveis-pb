package com.erp.server.responses;

import infra.global.relational.entities.StockEntity;

import java.util.List;

public record StocksResponse(
        List<StockResponse> stocks,
        int count
) {
    public StocksResponse(List<StockEntity> stockEntities) {
        this(
            stockEntities.stream().map(StockResponse::new).toList(),
            stockEntities.size()
        );
    }
}
