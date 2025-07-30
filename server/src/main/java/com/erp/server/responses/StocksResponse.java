package com.erp.server.responses;

import com.erp.server.entities.Stock;

import java.util.List;

public record StocksResponse(
        List<StockResponse> stocks,
        int count
) {
    public StocksResponse(List<Stock> stocks) {
        this(
            stocks.stream().map(StockResponse::new).toList(),
            stocks.size()
        );
    }
}
