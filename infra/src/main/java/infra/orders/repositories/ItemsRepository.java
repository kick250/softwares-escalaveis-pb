package infra.orders.repositories;

import application.orders.domain.Item;
import application.orders.exceptions.SomeItemsWereNotFoundException;
import application.orders.repositories.AllItems;
import infra.global.relational.repositories.StockItemsJpaRepository;
import infra.orders.mappers.ItemsMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class ItemsRepository implements AllItems {
    private final StockItemsJpaRepository stockItemsJpaRepository;
    private final ItemsMapper itemMapper;

    public ItemsRepository(StockItemsJpaRepository stockItemsJpaRepository, ItemsMapper itemMapper) {
        this.stockItemsJpaRepository = stockItemsJpaRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public List<Item> getByIds(Set<Long> ids) throws SomeItemsWereNotFoundException {
        var stockItems = stockItemsJpaRepository.findAllById(ids);

        if (stockItems.size() != ids.size()) throw new SomeItemsWereNotFoundException();

        return stockItems.stream().map(itemMapper::toDomain).toList();
    }

    @Override
    public void updateAll(List<Item> items) {
        items.forEach(this::update);
    }

    public void update(Item item) {
        var stockItemEntity = stockItemsJpaRepository.findById(item.getId()).orElse(null);
        var stockItemToUpdate = itemMapper.toEntity(item, stockItemEntity);

        stockItemsJpaRepository.save(stockItemToUpdate);
    }
}
