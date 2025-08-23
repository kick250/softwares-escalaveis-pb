package com.erp.server.requests;

public record OrderItemRequest (
        Long itemId,
        Integer quantity
) {

}
