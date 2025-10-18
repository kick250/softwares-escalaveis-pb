package infra.orders.mappers;

import application.orders.domain.Item;
import infra.global.relational.entities.StockItemEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemsMapper {

    public Item toDomain(StockItemEntity stockItemEntity) {
        return new Item(stockItemEntity.getId(), stockItemEntity.getName(), stockItemEntity.getStockId(), stockItemEntity.getQuantity(), stockItemEntity.getPrice());
    }

    public StockItemEntity toEntity(Item item, StockItemEntity stockItemEntity) {
        StockItemEntity stockItem = stockItemEntity == null ? new StockItemEntity() : stockItemEntity;

        stockItem.setId(item.getId());
        stockItem.setQuantity(item.getAvailableQuantity());

        return stockItem;
    }
}
