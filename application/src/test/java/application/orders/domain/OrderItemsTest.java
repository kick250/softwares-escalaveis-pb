package application.orders.domain;

import application.orders.exceptions.UnavailableItemQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderItemsTest {
    private List<OrderItem> items;

    @BeforeEach
    public void setUp() {
        OrderItem item1 = mock(OrderItem.class);
        when(item1.getPrice()).thenReturn(100.0);
        when(item1.getQuantity()).thenReturn(2);

        OrderItem item2 = mock(OrderItem.class);
        when(item2.getPrice()).thenReturn(50.0);
        when(item2.getQuantity()).thenReturn(1);

        items = List.of(item1, item2);
    }

    @Test
    public void testTotalAmount() {
        OrderItems orderItems = new OrderItems(items);

        assertEquals(250.0, orderItems.totalAmount());
    }

    @Test
    public void testDecreaseItemsQuantity() throws Exception {
        OrderItems orderItems = new OrderItems(items);

        orderItems.decreaseItemsQuantity();

        for (OrderItem item : items) verify(item, times(1)).decreaseItemQuantity();
    }

    @Test
    public void testDecreaseItemsQuantity_whenUnavailableQuantity() throws Exception {
        OrderItem orderItem = mock(OrderItem.class);
        Item item = mock(Item.class);
        when(item.getName()).thenReturn("Test Item");
        doThrow(new UnavailableItemQuantityException(item)).when(orderItem).decreaseItemQuantity();
        List<OrderItem> items = List.of(orderItem);

        OrderItems orderItems = new OrderItems(items);

        Exception exception = assertThrows(UnavailableItemQuantityException.class, orderItems::decreaseItemsQuantity);
        assertEquals("O stock não possui a quantidade disponível do item: " + item.getName(), exception.getMessage());
    }

    @Test
    public void testReturnItemsToStock() {
        OrderItems orderItems = new OrderItems(items);

        orderItems.returnItemsToStock();

        for (OrderItem item : items) verify(item, times(1)).returnToStock();
    }
}