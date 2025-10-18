package infra.global.relational.repositories;

import infra.global.relational.entities.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StocksJpaRepository extends JpaRepository<StockEntity, Long> {

    public List<StockEntity> findAllByDeletedFalse();

    public Optional<StockEntity> findByIdAndDeletedFalse(Long id);

    public int countAllByDeletedFalse();
}
