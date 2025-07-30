package com.erp.server.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StockCreateRequest(
        @NotNull
        @NotBlank
        String name
) {
}
