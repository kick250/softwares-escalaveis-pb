package com.erp.server.services;

import com.erp.server.exceptions.CartNotFoundException;
import infra.global.document.entities.Cart;
import infra.global.document.entities.CartItem;
import infra.global.document.repositories.CartsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class CartsService {
    private final CartsRepository cartsRepository;
    private final RedisTemplate<String, Boolean> redisBooleanTemplate;

    private static final String CART_CREATE_LOCK_KEY_PREFIX = "cart_create_lock_user_";

    public Cart getOrCreateCartByUserId(Long userId) throws CartNotFoundException {
        String lockKey = getCartCreateLockKeyForUserId(userId);

        boolean lockAcquired = tryLock(lockKey);

        if (!lockAcquired) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return cartsRepository.findByUserIdAndActiveTrue(userId).orElseThrow(CartNotFoundException::new);
        }

        try {
            Cart cart = cartsRepository.findByUserIdAndActiveTrue(userId).orElse(null);
            if (cart != null) return cart;

            return createCartFor(userId);

        } finally {
            unlock(lockKey);
        }
    }

    private boolean tryLock(String lockKey) {
        Boolean acquired = redisBooleanTemplate.opsForValue()
                .setIfAbsent(lockKey, true, 5, TimeUnit.SECONDS);

        return Boolean.TRUE.equals(acquired);
    }

    private void unlock(String lockKey) {
        redisBooleanTemplate.delete(lockKey);
    }

    private String getCartCreateLockKeyForUserId(Long userId) {
        return CART_CREATE_LOCK_KEY_PREFIX + userId;
    }

    private Cart createCartFor(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartsRepository.save(cart);
    }

    public void updateCartByUserId(Long userId, Long stockId, Map<Long, Integer> itemIdPerQuantity) throws CartNotFoundException {
        Cart cart = cartsRepository.findByUserIdAndActiveTrue(userId).orElseThrow(CartNotFoundException::new);
        cart.setStockId(stockId);
        var cartItemIdPerCartItem = cart.getItemIdPerCartItem();
        List<CartItem> newItems = new ArrayList<>();

        for (var entry : itemIdPerQuantity.entrySet()) {
            Long itemId = entry.getKey();
            Integer quantity = entry.getValue();
            CartItem cartItem = null;

            if (cartItemIdPerCartItem.containsKey(itemId)) {
                cartItem = cartItemIdPerCartItem.get(itemId);

            } else {
                cartItem = new CartItem();
                cartItem.setItemId(itemId);
            }
            cartItem.setQuantity(quantity);
            newItems.add(cartItem);

         }

        cart.setItems(newItems);
        cartsRepository.save(cart);
    }
}
