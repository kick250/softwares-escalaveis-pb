package com.erp.server.responses.portal;

import infra.global.entities.StockEntity;

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
