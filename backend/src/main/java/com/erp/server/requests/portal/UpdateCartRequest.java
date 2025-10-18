package com.erp.server.requests.portal;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record UpdateCartRequest(
        @NotNull
        Long stockId,
        @NotEmpty
        List<OrderItemRequest> items
) {
    public Map<Long, Integer> itemIdPerQuantity() {
        Map<Long, Integer> map = new HashMap<>();

        items.forEach(item -> {
            map.put(item.itemId(), item.quantity());
        });

        return map;
    }
}
