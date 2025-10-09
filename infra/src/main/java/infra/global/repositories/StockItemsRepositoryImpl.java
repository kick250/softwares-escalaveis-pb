package infra.global.repositories;

import infra.global.entities.StockItemEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StockItemsRepositoryImpl implements StockItemsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<StockItemEntity> findAllByStockIdAndDeletedFalse(Long stockId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<StockItemEntity> query = cb.createQuery(StockItemEntity.class);
        Root<StockItemEntity> root = query.from(StockItemEntity.class);

        Predicate byStockId = cb.equal(root.get("stock").get("id"), stockId);
        Predicate notDeleted = cb.isFalse(root.get("deleted"));

        query.select(root).where(cb.and(byStockId, notDeleted));

        return entityManager.createQuery(query).getResultList();
    }
}
