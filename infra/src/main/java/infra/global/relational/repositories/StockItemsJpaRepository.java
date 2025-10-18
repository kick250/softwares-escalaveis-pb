package infra.global.relational.repositories;

import infra.global.relational.entities.StockItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockItemsJpaRepository extends JpaRepository<StockItemEntity, Long>, StockItemsJpaRepositoryCustom {

    public List<StockItemEntity> findAllByDeletedFalse();

    public Optional<StockItemEntity> findByIdAndDeletedFalse(Long id);

    public int countAllByDeletedFalse();
}
