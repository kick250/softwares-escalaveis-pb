package com.erp.server.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockItemUpdateRequest(
        @NotNull
        @Positive
        Double price,
        @NotNull
        @Positive
        Integer quantity,
        @NotNull
        Long productId
) {
}
