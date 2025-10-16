package application.orders.useCases;

import application.orders.domain.Item;
import application.orders.domain.Order;
import application.orders.domain.User;
import application.orders.exceptions.InvalidItemStockException;
import application.orders.exceptions.OrderOwnerNotFoundException;
import application.orders.exceptions.SomeItemsWereNotFoundException;
import application.orders.exceptions.UserNotFoundException;
import application.orders.repositories.AllItems;
import application.orders.repositories.AllOrders;
import application.orders.repositories.AllUsers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CreateOrderTest {
    private CreateOrder createOrder;
    private AllOrders allOrders;
    private AllUsers allUsers;
    private AllItems allItems;

    private Map<Long, Integer> itemIdPerQuantity;

    @BeforeEach
    public void setUp() {
        allOrders = mock(AllOrders.class);
        allUsers = mock(AllUsers.class);
        allItems = mock(AllItems.class);
        createOrder = new CreateOrder(allOrders, allUsers, allItems);

        itemIdPerQuantity = Map.of(1L, 3, 2L, 6);
    }

    @Test
    public void testExecute() throws Exception {
        Long userId = 1L;
        Long stockId = 2L;

        Item item1 = mock(Item.class);
        when(item1.getId()).thenReturn(1L);
        when(item1.isFromStock(stockId)).thenReturn(true);
        when(item1.getPrice()).thenReturn(10.0);

        Item item2 = mock(Item.class);
        when(item2.getId()).thenReturn(2L);
        when(item2.isFromStock(stockId)).thenReturn(true);
        when(item2.getPrice()).thenReturn(20.0);

        when(allItems.getByIds(itemIdPerQuantity.keySet())).thenReturn(List.of(item1, item2));
        when(allUsers.getById(userId)).thenReturn(mock(User.class));

        createOrder.execute(userId, stockId, itemIdPerQuantity);

        verify(allOrders, times(1)).create(any(Order.class));
        verify(allItems, times(1)).updateAll(any());
    }

    @Test
    public void testExecute_whenUserNotFound() throws Exception {
        Long userId = 1L;
        Long stockId = 2L;

        doThrow(new UserNotFoundException()).when(allUsers).getById(userId);

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            createOrder.execute(userId, stockId, itemIdPerQuantity);
        });

        verify(allOrders, never()).create(any(Order.class));
        verify(allItems, never()).updateAll(any());

        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    public void testExecute_whenSomeItemsNotFound() throws Exception {
        Long userId = 1L;
        Long stockId = 2L;

        doThrow(new SomeItemsWereNotFoundException()).when(allItems).getByIds(itemIdPerQuantity.keySet());
        when(allUsers.getById(userId)).thenReturn(mock(User.class));

        Exception exception = assertThrows(SomeItemsWereNotFoundException.class, () -> {
            createOrder.execute(userId, stockId, itemIdPerQuantity);
        });

        verify(allOrders, never()).create(any(Order.class));
        verify(allItems, never()).updateAll(any());

        assertEquals("Alguns itens do pedido não foram encontrados", exception.getMessage());
    }

    @Test
    public void testExecute_whenItemNotFromStock() throws Exception {
        Long userId = 1L;
        Long stockId = 2L;

        Item item1 = mock(Item.class);
        when(item1.getId()).thenReturn(1L);
        when(item1.isFromStock(stockId)).thenReturn(true);

        Item item2 = mock(Item.class);
        when(item2.getId()).thenReturn(2L);
        when(item2.isFromStock(stockId)).thenReturn(false);

        when(allItems.getByIds(itemIdPerQuantity.keySet())).thenReturn(List.of(item1, item2));
        when(allUsers.getById(userId)).thenReturn(mock(User.class));

        Exception exception = assertThrows(InvalidItemStockException.class, () -> {
            createOrder.execute(userId, stockId, itemIdPerQuantity);
        });

        verify(allOrders, never()).create(any(Order.class));
        verify(allItems, never()).updateAll(any());

        assertEquals("Um ou mais itens do pedido estão com estoque inválido", exception.getMessage());
    }

    @Test
    public void testExecute_whenOrderOwnerNotFound() throws Exception {
        Long userId = 1L;
        Long stockId = 2L;

        Item item1 = mock(Item.class);
        when(item1.getId()).thenReturn(1L);
        when(item1.isFromStock(stockId)).thenReturn(true);
        when(item1.getPrice()).thenReturn(10.0);

        Item item2 = mock(Item.class);
        when(item2.getId()).thenReturn(2L);
        when(item2.isFromStock(stockId)).thenReturn(true);
        when(item2.getPrice()).thenReturn(20.0);

        when(allItems.getByIds(itemIdPerQuantity.keySet())).thenReturn(List.of(item1, item2));
        when(allUsers.getById(userId)).thenReturn(mock(User.class));

        doThrow(new OrderOwnerNotFoundException()).when(allOrders).create(any(Order.class));

        Exception exception = assertThrows(OrderOwnerNotFoundException.class, () -> createOrder.execute(userId, stockId, itemIdPerQuantity));

        verify(allOrders, times(1)).create(any(Order.class));
        verify(allItems, times(0)).updateAll(any());
    }
}