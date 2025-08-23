package infra.orders.mappers;

import application.orders.domain.Item;
import infra.global.entities.StockItemEntity;
import org.springframework.stereotype.Component;

@Component
public class ItemsMapper {

    public Item toDomain(StockItemEntity stockItemEntity) {
        return new Item(stockItemEntity.getId(), stockItemEntity.getStockId(), stockItemEntity.getPrice());
    }
}
