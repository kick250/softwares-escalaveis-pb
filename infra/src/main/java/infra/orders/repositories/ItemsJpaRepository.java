package infra.orders.repositories;

import application.orders.domain.Item;
import application.orders.exceptions.SomeItemsWereNotFoundException;
import application.orders.repositories.AllItems;
import infra.global.repositories.StockItemsRepository;
import infra.orders.mappers.ItemsMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class ItemsJpaRepository implements AllItems {
    private final StockItemsRepository stockItemsRepository;
    private final ItemsMapper itemMapper;

    public ItemsJpaRepository(StockItemsRepository stockItemsRepository, ItemsMapper itemMapper) {
        this.stockItemsRepository = stockItemsRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public List<Item> getByIds(Set<Long> ids) throws SomeItemsWereNotFoundException {
        var stockItems = stockItemsRepository.findAllById(ids);

        if (stockItems.size() != ids.size()) throw new SomeItemsWereNotFoundException();

        return stockItems.stream().map(itemMapper::toDomain).toList();
    }

    @Override
    public void updateAll(List<Item> items) {
        items.forEach(this::update);
    }

    public void update(Item item) {
        var stockItemEntity = stockItemsRepository.findById(item.getId()).orElse(null);
        var stockItemToUpdate = itemMapper.toEntity(item, stockItemEntity);

        stockItemsRepository.save(stockItemToUpdate);
    }
}
