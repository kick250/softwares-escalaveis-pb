package application.orders.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTest {
    private User adminUser;
    private User regularUser;

    @BeforeEach
    public void setUp() {
        adminUser = new User(1L, true);
        regularUser = new User(2L, false);
    }

    @Test
    public void testIsAdmin_whenIsAdmin() {
        assertTrue(adminUser.isAdmin());
    }

    @Test
    public void testIsAdmin_whenIsNotAdmin() {
        assertFalse(regularUser.isAdmin());
    }

    @Test
    public void testExceedQuota_whenAdminUser() {
        OrderItems items = mock(OrderItems.class);
        when(items.totalAmount()).thenReturn(20000.0);

        assertFalse(adminUser.exceedQuota(items));
    }

    @Test
    public void testExceedQuota_whenRegularUser_whenWithinQuota() {
        OrderItems items = mock(OrderItems.class);
        when(items.totalAmount()).thenReturn(5000.0);

        assertFalse(regularUser.exceedQuota(items));
    }

    @Test
    public void testExceedQuota_whenRegularUser_whenExceedsQuota() {
        OrderItems items = mock(OrderItems.class);
        when(items.totalAmount()).thenReturn(15000.0);

        assertTrue(regularUser.exceedQuota(items));
    }
}