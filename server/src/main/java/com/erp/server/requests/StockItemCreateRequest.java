package com.erp.server.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockItemCreateRequest(
        @NotNull
        @Positive
        Double price,
        @NotNull
        @Positive
        Integer quantity,
        @NotNull
        Long productId,
        @NotNull
        Long stockId
) {
}
