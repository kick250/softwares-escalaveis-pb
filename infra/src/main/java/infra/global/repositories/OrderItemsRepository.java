package infra.global.repositories;

import infra.global.entities.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItemEntity, Long> {

}
