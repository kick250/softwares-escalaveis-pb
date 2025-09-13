package com.erp.server.controllers.portal;

import application.orders.exceptions.*;
import application.orders.useCases.CreateOrder;
import com.erp.server.requests.OrderCreateRequest;
import infra.global.entities.UserEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portal/orders")
@AllArgsConstructor
public class OrdersController {
    private final CreateOrder createOrder;

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@AuthenticationPrincipal UserEntity currentUser, @Valid @RequestBody OrderCreateRequest request) {
        try {
            createOrder.execute(currentUser.getId(), request.stockId(), request.itemIdPerQuantity());
            return ResponseEntity.status(201).body("Pedido criado com sucesso.");
        } catch (InvalidItemStockException | UnavailableItemQuantityException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (OrderOwnerNotFoundException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body("Usuário inválido.");
        } catch (SomeItemsWereNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}