package com.erp.server.responses.portal;

import infra.global.document.entities.Cart;

import java.util.List;

public record CartResponse(
        Long stockId,
        List<CartItemResponse> items
) {
    public CartResponse(Cart cart) {
        this(
                cart.getStockId(),
                cart.getItems().stream().map(CartItemResponse::new).toList()
        );
    }
}
