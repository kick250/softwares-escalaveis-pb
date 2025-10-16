package application.orders.useCases;

import application.orders.domain.Order;
import application.orders.domain.User;
import application.orders.exceptions.InsufficientPermissionException;
import application.orders.exceptions.InvalidStateToApproveException;
import application.orders.exceptions.OrderNotFoundException;
import application.orders.exceptions.UserNotFoundException;
import application.orders.repositories.AllOrders;
import application.orders.repositories.AllUsers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ApproveOrderTest {
    private ApproveOrder approveOrder;
    private AllOrders allOrders;
    private AllUsers allUsers;

    @BeforeEach
    public void setUp() {
        allOrders = mock(AllOrders.class);
        allUsers = mock(AllUsers.class);
        approveOrder = new ApproveOrder(allOrders, allUsers);
    }

    @Test
    public void testExecute() throws Exception {
        Long userId = 1L;
        Long orderId = 2L;
        User author = mock(User.class);
        Order order = mock(Order.class);

        when(allUsers.getById(userId)).thenReturn(author);
        when(allOrders.getById(orderId)).thenReturn(order);

        approveOrder.execute(userId, orderId);

        verify(order).approve(author);
        verify(allOrders).update(order);
    }

    @Test
    public void testExecute_whenUserNotFound() throws Exception {
        Long userId = 1L;
        Long orderId = 2L;

        when(allUsers.getById(userId)).thenThrow(new UserNotFoundException());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            approveOrder.execute(userId, orderId);
        });

        verify(allOrders, never()).getById(anyLong());
        verify(allOrders, never()).update(any(Order.class));
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    public void testExecute_whenOrderNotFound() throws Exception {
        Long userId = 1L;
        Long orderId = 2L;
        User author = mock(User.class);

        when(allUsers.getById(userId)).thenReturn(author);
        when(allOrders.getById(orderId)).thenThrow(new OrderNotFoundException());

        Exception exception = assertThrows(OrderNotFoundException.class, () -> {
            approveOrder.execute(userId, orderId);
        });

        verify(allOrders, never()).update(any(Order.class));
        assertEquals("Pedido não encontrado", exception.getMessage());
    }

    @Test
    public void testExecute_whenInsufficientPermission() throws Exception {
        Long userId = 1L;
        Long orderId = 2L;
        User author = mock(User.class);
        Order order = mock(Order.class);

        doThrow(new InsufficientPermissionException()).when(order).approve(author);
        when(allUsers.getById(userId)).thenReturn(author);
        when(allOrders.getById(orderId)).thenReturn(order);

        Exception exception = assertThrows(InsufficientPermissionException.class, () -> {
            approveOrder.execute(userId, orderId);
        });

        verify(allOrders, never()).update(any(Order.class));
        assertEquals("O usuário não tem permissão para realizar esta ação.", exception.getMessage());
    }

    @Test
    public void testExecute_whenInvalidStateToApprove() throws Exception {
        Long userId = 1L;
        Long orderId = 2L;
        User author = mock(User.class);
        Order order = mock(Order.class);

        doThrow(new InvalidStateToApproveException()).when(order).approve(author);
        when(allUsers.getById(userId)).thenReturn(author);
        when(allOrders.getById(orderId)).thenReturn(order);

        Exception exception = assertThrows(InvalidStateToApproveException.class, () -> {
            approveOrder.execute(userId, orderId);
        });

        verify(allOrders, never()).update(any(Order.class));
        assertEquals("Pedido não está em um estado válido para aprovação.", exception.getMessage());
    }
}