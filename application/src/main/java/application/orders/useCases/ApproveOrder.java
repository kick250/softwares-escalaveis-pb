package application.orders.useCases;

import application.orders.exceptions.InsufficientPermissionException;
import application.orders.exceptions.InvalidStateToApproveException;
import application.orders.exceptions.OrderNotFoundException;
import application.orders.exceptions.UserNotFoundException;
import application.orders.repositories.AllOrders;
import application.orders.repositories.AllUsers;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApproveOrder {
    private final AllOrders allOrders;
    private final AllUsers allUsers;

    public void execute(Long userId, Long orderId) throws UserNotFoundException, OrderNotFoundException, InsufficientPermissionException, InvalidStateToApproveException {
        var author = allUsers.getById(userId);
        var order = allOrders.getById(orderId);

        order.approve(author);
        allOrders.update(order);
    }
}
