package com.erp.server.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StockUpdateRequest(
        @NotNull
        @NotBlank
        String name
) {
}
