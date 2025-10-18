package com.erp.server.responses.portal;

import infra.global.document.entities.CartItem;

public record CartItemResponse(
        Long itemId,
        int quantity
) {
    public CartItemResponse(CartItem cartItem) {
        this(
                cartItem.getItemId(),
                cartItem.getQuantity()
        );
    }
}
