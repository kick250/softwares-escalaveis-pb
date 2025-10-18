package infra.global.relational.repositories;

import infra.global.relational.entities.StockItemEntity;

import java.util.List;

public interface StockItemsJpaRepositoryCustom {
    List<StockItemEntity> findAllByStockIdAndDeletedFalse(Long stockId);
}