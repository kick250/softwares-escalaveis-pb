package application.orders.actions;

import application.orders.domain.*;
import application.orders.exceptions.InvalidItemStockException;
import application.orders.exceptions.OrderOwnerNotFoundException;
import application.orders.exceptions.SomeItemsWereNotFoundException;
import application.orders.exceptions.UserNotFoundException;
import application.orders.repositories.AllOrders;
import application.orders.repositories.AllItems;
import application.orders.repositories.AllUsers;

import java.util.List;
import java.util.Map;

public class CreateOrder {
    private final AllOrders allOrders;
    private final AllUsers allUsers;
    private final AllItems allItems;

    public CreateOrder(AllOrders allOrders, AllUsers allUsers, AllItems allItems) {
        this.allOrders = allOrders;
        this.allUsers = allUsers;
        this.allItems = allItems;
    }

    public void execute(Long userId, Long stockId, Map<Long, Integer> itemIdPerQuantity) throws OrderOwnerNotFoundException, UserNotFoundException, InvalidItemStockException, SomeItemsWereNotFoundException {
        User owner = allUsers.getById(userId);
        List<Item> items = allItems.getByIds(itemIdPerQuantity.keySet());

        checkItems(items, stockId);
        OrderItems orderItems = buildOrderItems(items, itemIdPerQuantity);

        Order order = new Order(owner, stockId, orderItems);

        allOrders.create(order);
    }

    private void checkItems(List<Item> items, Long stockId) throws InvalidItemStockException {
        if (items.stream().anyMatch(item -> !item.isFromStock(stockId)))
            throw new InvalidItemStockException();
    }

    private OrderItems buildOrderItems(List<Item> items, Map<Long, Integer> itemIdPerQuantity) {
        List<OrderItem> orderItems = items.stream().map(item -> {
            Integer quantity = itemIdPerQuantity.get(item.getId());
            return new OrderItem(null, item, quantity, item.getPrice());
        }).toList();

        return new OrderItems(orderItems);
    }
}
