package infra.global.repositories;

import infra.global.entities.StockItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockItemsRepository extends JpaRepository<StockItemEntity, Long>, StockItemsRepositoryCustom {

    public List<StockItemEntity> findAllByDeletedFalse();

    public Optional<StockItemEntity> findByIdAndDeletedFalse(Long id);

    public int countAllByDeletedFalse();
}
