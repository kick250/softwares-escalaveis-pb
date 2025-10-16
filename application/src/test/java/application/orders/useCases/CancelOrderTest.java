package application.orders.useCases;

import application.orders.domain.Item;
import application.orders.domain.Order;
import application.orders.domain.User;
import application.orders.exceptions.OrderNotFoundException;
import application.orders.exceptions.UserNotFoundException;
import application.orders.repositories.AllItems;
import application.orders.repositories.AllOrders;
import application.orders.repositories.AllUsers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CancelOrderTest {
    private CancelOrder cancelOrder;
    private AllOrders allOrders;
    private AllUsers allUsers;
    private AllItems allItems;

    @BeforeEach
    public void setUp() {
        allOrders = mock(AllOrders.class);
        allUsers = mock(AllUsers.class);
        allItems = mock(AllItems.class);

        cancelOrder = new CancelOrder(allOrders, allUsers, allItems);
    }

    @Test
    public void testExecute() throws Exception {
        Long userId = 1L;
        Long orderId = 1L;
        User author = mock(User.class);
        Order order = mock(Order.class);
        List<Item> items = mock(List.class);

        when(allUsers.getById(userId)).thenReturn(author);
        when(allOrders.getById(orderId)).thenReturn(order);
        when(order.getItems()).thenReturn(items);

        cancelOrder.execute(userId, orderId);

        verify(order).cancel(author);
        verify(allOrders).update(order);
        verify(allItems).updateAll(items);
    }

    @Test
    public void testExecute_whenUserNotFound() throws Exception {
        Long userId = 1L;
        Long orderId = 1L;

        doThrow(new UserNotFoundException()).when(allUsers).getById(userId);

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            cancelOrder.execute(userId, orderId);
        });

        verify(allOrders, never()).update(any(Order.class));
        verify(allItems, never()).updateAll(anyList());
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    public void testExecute_whenOrderNotFound() throws Exception {
        Long userId = 1L;
        Long orderId = 1L;
        User author = mock(User.class);

        when(allUsers.getById(userId)).thenReturn(author);
        doThrow(new OrderNotFoundException()).when(allOrders).getById(orderId);

        Exception exception = assertThrows(OrderNotFoundException.class, () -> {
            cancelOrder.execute(userId, orderId);
        });

        verify(allOrders, never()).update(any(Order.class));
        verify(allItems, never()).updateAll(anyList());
        assertEquals("Pedido não encontrado", exception.getMessage());
    }
}