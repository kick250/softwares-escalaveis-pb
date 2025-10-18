package com.erp.server.responses.portal;

import application.orders.enums.OrderStatus;
import infra.global.relational.entities.OrderEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record OrderResponse (
        Long id,
        OrderStatus status,
        String translatedStatus,
        String stockName,
        String ownerName,
        String createdAt,
        List<OrderItemResponse> items
) {
    public OrderResponse(OrderEntity orderEntity) {
        this(
                orderEntity.getId(),
                orderEntity.getStatus(),
                orderEntity.getStatus().getTranslated(),
                orderEntity.getStockName(),
                orderEntity.getOwnerName(),
                formatDate(orderEntity.getCreatedAt()),
                orderEntity.getItems().stream().map(OrderItemResponse::new).toList()
        );
    }

    private static String formatDate(Instant instant) {
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm").format(ldt);
    }

}
