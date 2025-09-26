package infra.global.repositories;

import infra.global.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<ProductEntity, Long> {

    public List<ProductEntity> findAllByDeletedFalse();

    public Optional<ProductEntity> findByIdAndDeletedFalse(Long id);
}
