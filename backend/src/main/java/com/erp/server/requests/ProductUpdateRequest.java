package com.erp.server.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductUpdateRequest(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        String description
) {
}
