package application.orders.useCases;

import application.orders.domain.Order;
import application.orders.domain.User;
import application.orders.exceptions.InsufficientPermissionException;
import application.orders.exceptions.InvalidStateToCancelException;
import application.orders.exceptions.OrderNotFoundException;
import application.orders.exceptions.UserNotFoundException;
import application.orders.repositories.AllItems;
import application.orders.repositories.AllOrders;
import application.orders.repositories.AllUsers;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CancelOrder {
    private final AllOrders allOrders;
    private final AllUsers allUsers;
    private final AllItems allItems;

    public void execute(Long userId, Long orderId) throws UserNotFoundException, OrderNotFoundException, InsufficientPermissionException, InvalidStateToCancelException {
        User author = allUsers.getById(userId);
        Order order = allOrders.getById(orderId);

        order.cancel(author);

        allOrders.update(order);
        allItems.updateAll(order.getItems());
    }
}
