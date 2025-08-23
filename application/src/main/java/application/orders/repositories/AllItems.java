package application.orders.repositories;

import application.orders.domain.Item;
import application.orders.exceptions.SomeItemsWereNotFoundException;

import java.util.List;
import java.util.Set;

public interface AllItems {

    public List<Item> getByIds(Set<Long> ids) throws SomeItemsWereNotFoundException;
}
