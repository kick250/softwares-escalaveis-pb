package com.erp.server.controllers.portal;

import com.erp.server.exceptions.CartNotFoundException;
import com.erp.server.requests.portal.UpdateCartRequest;
import com.erp.server.responses.portal.CartResponse;
import com.erp.server.services.CartsService;
import infra.global.document.entities.Cart;
import infra.global.relational.entities.UserEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portal/carts")
@AllArgsConstructor
public class CartsController {
    private CartsService cartsService;

    @GetMapping
    public ResponseEntity<?> getCart(@AuthenticationPrincipal UserEntity currentUser) {
        try {
            Cart cart = cartsService.getOrCreateCartByUserId(currentUser.getId());
            return ResponseEntity.ok(new CartResponse(cart));
        } catch (CartNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCart(@AuthenticationPrincipal UserEntity currentUser, @Valid @RequestBody UpdateCartRequest request) {
        try {
            cartsService.updateCartByUserId(currentUser.getId(), request.stockId(), request.itemIdPerQuantity());
            return ResponseEntity.ok("Carrinho atualizado com sucesso.");
        } catch (CartNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
