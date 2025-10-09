package com.erp.server.responses.portal;

import infra.global.entities.OrderItemEntity;

public record OrderItemResponse(
        Long id,
        String name,
        Integer quantity,
        Double pricePerUnit,
        Double totalPrice
) {
    public OrderItemResponse(OrderItemEntity orderItemEntity) {
        this(
                orderItemEntity.getId(),
                orderItemEntity.getName(),
                orderItemEntity.getQuantity(),
                orderItemEntity.getPrice(),
                orderItemEntity.getTotalPrice()
        );
    }
}
