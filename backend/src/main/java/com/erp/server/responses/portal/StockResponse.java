package com.erp.server.responses.portal;

import infra.global.relational.entities.StockEntity;

public record StockResponse(
        Long id,
        String name
) {
    public StockResponse(StockEntity stock) {
        this(
                stock.getId(),
                stock.getName()
        );
    }
}
