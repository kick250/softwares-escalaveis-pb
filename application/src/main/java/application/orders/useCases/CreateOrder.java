package application.orders.useCases;

import application.orders.domain.*;
import application.orders.exceptions.*;
import application.orders.repositories.AllCarts;
import application.orders.repositories.AllOrders;
import application.orders.repositories.AllItems;
import application.orders.repositories.AllUsers;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CreateOrder {
    private final AllOrders allOrders;
    private final AllUsers allUsers;
    private final AllItems allItems;
    private final AllCarts allCarts;

    public void execute(Long userId, Long stockId, Map<Long, Integer> itemIdPerQuantity) throws OrderOwnerNotFoundException, UserNotFoundException, InvalidItemStockException, SomeItemsWereNotFoundException, UnavailableItemQuantityException {
        User owner = allUsers.getById(userId);
        List<Item> items = allItems.getByIds(itemIdPerQuantity.keySet());

        checkItems(items, stockId, itemIdPerQuantity);
        OrderItems orderItems = buildOrderItems(items, itemIdPerQuantity);

        Order order = new Order(owner, stockId, orderItems);

        order.updateStocks();

        allOrders.create(order);
        allItems.updateAll(order.getItems());
        allCarts.deactivateCart(userId);
    }

    private void checkItems(List<Item> items, Long stockId, Map<Long, Integer> itemIdPerQuantity) throws InvalidItemStockException, UnavailableItemQuantityException {
        for (Item item : items) {
            if (!item.isFromStock(stockId)) throw new InvalidItemStockException();
        }
    }

    private OrderItems buildOrderItems(List<Item> items, Map<Long, Integer> itemIdPerQuantity) {
        List<OrderItem> orderItems = items.stream().map(item -> {
            Integer quantity = itemIdPerQuantity.get(item.getId());
            return new OrderItem(null, item, quantity, item.getPrice());
        }).toList();

        return new OrderItems(orderItems);
    }
}
