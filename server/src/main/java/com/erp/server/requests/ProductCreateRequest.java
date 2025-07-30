package com.erp.server.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCreateRequest(
        @NotNull
        @NotBlank
        String name,
        @NotNull
        @NotBlank
        String description
) {
}
