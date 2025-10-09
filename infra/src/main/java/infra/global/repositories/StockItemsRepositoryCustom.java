package infra.global.repositories;

import infra.global.entities.StockItemEntity;

import java.util.List;

public interface StockItemsRepositoryCustom {
    List<StockItemEntity> findAllByStockIdAndDeletedFalse(Long stockId);
}