package infra.global.document.repositories;

import infra.global.document.entities.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartsRepository extends MongoRepository<Cart, String> {

    Optional<Cart> findByUserIdAndActiveTrue(Long userId);
}
