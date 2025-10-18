package infra.global.document.repositories;

import infra.global.document.entities.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartItemsRepository extends MongoRepository<CartItem, String> {

}
