package com.erp.server.requests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record OrderCreateRequest(
        Long stockId,
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
