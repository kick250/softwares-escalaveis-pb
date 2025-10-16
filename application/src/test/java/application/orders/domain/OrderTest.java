package application.orders.domain;

import application.orders.enums.OrderStatus;
import application.orders.exceptions.InsufficientPermissionException;
import application.orders.exceptions.InvalidStateToApproveException;
import application.orders.exceptions.InvalidStateToCancelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderTest {
    private User owner;
    private OrderItems items;

    @BeforeEach
    public void setUp() {
        owner = mock(User.class);
        items = mock(OrderItems.class);
    }

    @Test
    public void testOrderCreation_whenWithinQuote() {
        when(owner.exceedQuota(items)).thenReturn(false);

        Order order = new Order(owner, 1L, items);

        assertEquals(OrderStatus.APPROVED, order.getStatus());
    }

    @Test
    public void testOrderCreation_whenExceedQuote() {
        when(owner.exceedQuota(items)).thenReturn(true);

        Order order = new Order(owner, 1L, items);

        assertEquals(OrderStatus.WAITING_APPROVAL, order.getStatus());
    }

    @Test
    public void testUpdateStocks_whenNotUpdated() throws Exception {
        Order order = new Order(owner, 2L, items);

        order.updateStocks();

        verify(items, times(1)).decreaseItemsQuantity();
    }

    @Test
    public void testUpdateStocks_whenAlreadyUpdated() throws Exception {
        Order order = new Order(owner, 2L, items);

        order.updateStocks();
        order.updateStocks();

        verify(items, times(1)).decreaseItemsQuantity();
    }

    @Test
    public void testApprove_whenIsAdmin_whenStatusIsWaitingApproval() throws Exception {
        User author = mock(User.class);
        when(author.isAdmin()).thenReturn(true);
        Order order = new Order(1L, OrderStatus.WAITING_APPROVAL, owner, Instant.now(), 2L, items);

        order.approve(author);

        assertEquals(OrderStatus.APPROVED, order.getStatus());
    }

    @Test
    public void testApprove_whenIsNotAdmin_whenStatusIsWaitingApproval() throws Exception {
        User author = mock(User.class);
        when(author.isAdmin()).thenReturn(false);
        Order order = new Order(1L, OrderStatus.WAITING_APPROVAL, owner, Instant.now(), 2L, items);

        Exception exception = assertThrows(InsufficientPermissionException.class, () -> order.approve(author));

        assertEquals("O usuário não tem permissão para realizar esta ação.", exception.getMessage());
    }

    @Test
    public void testApprove_whenIsAdmin_whenStatusIsApproved() throws Exception {
        User author = mock(User.class);
        when(author.isAdmin()).thenReturn(true);
        Order order = new Order(1L, OrderStatus.APPROVED, owner, Instant.now(), 2L, items);

        Exception exception = assertThrows(InvalidStateToApproveException.class, () -> order.approve(author));

        assertEquals("Pedido não está em um estado válido para aprovação.", exception.getMessage());
    }

    @Test
    public void testCancel_whenIsAdmin_whenStatusIsWaitingApproval() throws Exception {
        User author = mock(User.class);
        when(author.isAdmin()).thenReturn(true);
        Order order = new Order(1L, OrderStatus.WAITING_APPROVAL, owner, Instant.now(), 2L, items);

        order.cancel(author);

        assertEquals(OrderStatus.CANCELED, order.getStatus());
    }

    @Test
    public void testCancel_whenIsNotAdmin_whenStatusIsWaitingApproval() throws Exception {
        User author = mock(User.class);
        when(author.isAdmin()).thenReturn(false);
        Order order = new Order(1L, OrderStatus.WAITING_APPROVAL, owner, Instant.now(), 2L, items);

        Exception exception = assertThrows(InsufficientPermissionException.class, () -> order.cancel(author));

        assertEquals("O usuário não tem permissão para realizar esta ação.", exception.getMessage());
    }

    @Test
    public void testCancel_whenIsAdmin_whenStatusIsApproved() throws Exception {
        User author = mock(User.class);
        when(author.isAdmin()).thenReturn(true);
        Order order = new Order(1L, OrderStatus.APPROVED, owner, Instant.now(), 2L, items);

        Exception exception = assertThrows(InvalidStateToCancelException.class, () -> order.cancel(author));

        assertEquals("Esse pedido não pode ser cancelado no estado atual.", exception.getMessage());
    }
}