package application.orders.domain;

import application.orders.exceptions.UnavailableItemQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderItemTest {
    private Item item;

    @BeforeEach
    public void setUp() {
        item = mock(Item.class);
    }

    @Test
    public void testDecreaseItemQuantity() throws Exception {
        OrderItem orderItem = new OrderItem(1L, item, 2, 100.0);

        orderItem.decreaseItemQuantity();

        verify(item).decreaseAvailableQuantity(2);
    }

    @Test
    public void testDecreaseItemQuantity_whenUnavailableItemQuantityException() throws Exception {
        OrderItem orderItem = new OrderItem(1L, item, 2, 100.0);

        when(item.getName()).thenReturn("Test Item");
        doThrow(new UnavailableItemQuantityException(item)).when(item).decreaseAvailableQuantity(2);

        Exception exception = assertThrows(UnavailableItemQuantityException.class, orderItem::decreaseItemQuantity);
        verify(item).decreaseAvailableQuantity(2);
        assertEquals("O stock não possui a quantidade disponível do item: " + item.getName(), exception.getMessage());
    }

    @Test
    public void testReturnToStock() {
        OrderItem orderItem = new OrderItem(1L, item, 3, 150.0);

        orderItem.returnToStock();

        verify(item).increaseAvailableQuantity(3);
    }
}