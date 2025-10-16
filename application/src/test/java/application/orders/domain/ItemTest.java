package application.orders.domain;

import application.orders.exceptions.UnavailableItemQuantityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    public void testGetStockId() {
        Long stockId = 100L;
        Item item = new Item(1L, "Test Item", stockId, 10, 20.0);

        assertEquals(stockId, item.getStockId());
    }

    @Test
    public void testIsFromStock() {
        Long stockId = 100L;
        Item item = new Item(1L, "Test Item", stockId, 10, 20.0);

        assertTrue(item.isFromStock(stockId));
        assertFalse(item.isFromStock(200L));
    }

    @Test
    public void testHasAvailableQuantity() {
        Item item = new Item(1L, "Test Item", 100L, 10, 20.0);

        assertTrue(item.hasAvailableQuantity(5));
        assertTrue(item.hasAvailableQuantity(10));
        assertFalse(item.hasAvailableQuantity(15));
    }

    @Test
    public void testIncreaseAvailableQuantity() {
        Item item = new Item(1L, "Test Item", 100L, 10, 20.0);
        item.increaseAvailableQuantity(5);

        assertEquals(15, item.getAvailableQuantity());
    }

    @Test
    public void testDecreaseAvailableQuantity_whenQuantityIsAvailable() throws UnavailableItemQuantityException {
        Item item = new Item(1L, "Test Item", 100L, 10, 20.0);

        item.decreaseAvailableQuantity(5);

        assertEquals(5, item.getAvailableQuantity());
    }

    @Test
    public void testDecreaseAvailableQuantity_whenQuantityIsNotAvailable() {
        Item item = new Item(1L, "Test Item", 100L, 10, 20.0);

        Exception exception = assertThrows(UnavailableItemQuantityException.class, () -> {
            item.decreaseAvailableQuantity(15);
        });

        assertEquals(10, item.getAvailableQuantity());
        assertEquals("O stock não possui a quantidade disponível do item: " + item.getName(), exception.getMessage());
    }
}