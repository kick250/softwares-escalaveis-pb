package com.erp.server.controllers.portal;

import application.orders.actions.CreateOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portal/orders")
public class OrdersController {
    private final CreateOrder createOrder;

    public OrdersController(CreateOrder createOrder) {
        this.createOrder = createOrder;
    }

    @PostMapping
    public ResponseEntity<?> createOrder() {;
        createOrder.execute();
        return ResponseEntity.ok().body("teste");
    }
}
