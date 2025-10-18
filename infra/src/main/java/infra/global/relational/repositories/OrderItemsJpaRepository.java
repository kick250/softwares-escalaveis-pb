package infra.global.relational.repositories;

import infra.global.relational.entities.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsJpaRepository extends JpaRepository<OrderItemEntity, Long> {

}
