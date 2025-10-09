package com.erp.server.controllers.portal;

import application.orders.exceptions.*;
import application.orders.useCases.ApproveOrder;
import application.orders.useCases.CancelOrder;
import application.orders.useCases.CreateOrder;
import com.erp.server.requests.OrderCreateRequest;
import com.erp.server.responses.portal.OrderResponse;
import com.erp.server.services.OrdersService;
import infra.global.entities.OrderEntity;
import infra.global.entities.UserEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portal/orders")
@AllArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;
    private final CreateOrder createOrder;
    private final ApproveOrder approveOrder;
    private final CancelOrder cancelOrder;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll() {
        List<OrderEntity> orderEntities = ordersService.getAll();
        return ResponseEntity.ok(orderEntities.stream().map(OrderResponse::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            OrderEntity orderEntity = ordersService.getById(id);
            return ResponseEntity.ok(new OrderResponse(orderEntity));
        } catch (com.erp.server.exceptions.OrderNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@AuthenticationPrincipal UserEntity currentUser, @Valid @RequestBody OrderCreateRequest request) {
        try {
            createOrder.execute(currentUser.getId(), request.stockId(), request.itemIdPerQuantity());
            return ResponseEntity.ok().body("Pedido criado com sucesso.");
        } catch (InvalidItemStockException | UnavailableItemQuantityException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (OrderOwnerNotFoundException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body("Usuário inválido.");
        } catch (SomeItemsWereNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/approve")
    @Transactional
    public ResponseEntity<?> approve(@AuthenticationPrincipal UserEntity currentUser, @PathVariable Long id) {
        try {
            approveOrder.execute(currentUser.getId(), id);
            return ResponseEntity.status(201).body("Pedido aprovado com sucesso.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("Usuário inválido.");
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (InvalidStateToApproveException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InsufficientPermissionException e) {
            return  ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/cancel")
    @Transactional
    public ResponseEntity<?> cancel(@AuthenticationPrincipal UserEntity currentUser, @PathVariable Long id) {
        try {
            cancelOrder.execute(currentUser.getId(), id);
            return ResponseEntity.ok("Pedido cancelado com sucesso.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("Usuário inválido.");
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (InsufficientPermissionException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (InvalidStateToCancelException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}