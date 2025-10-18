package infra.orders.repositories;

import application.orders.repositories.AllCarts;
import infra.global.document.entities.Cart;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component("OrdersCartsRepository")
@AllArgsConstructor
public class CartsRepository implements AllCarts {
    private final infra.global.document.repositories.CartsRepository cartsRepository;

    @Override
    public void deactivateCart(Long userId) {
        Cart cart = cartsRepository.findByUserIdAndActiveTrue(userId).orElse(null);
        if (cart != null) {
            cart.deactivate();
            cartsRepository.save(cart);
        }
    }
}
