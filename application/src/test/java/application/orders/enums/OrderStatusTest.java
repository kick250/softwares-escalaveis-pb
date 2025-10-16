package application.orders.enums;

import org.junit.jupiter.api.Test;

public class OrderStatusTest {

    @Test
    public void testGetTranslated() {
        assert OrderStatus.WAITING_APPROVAL.getTranslated().equals("Aguardando aprovação");
        assert OrderStatus.CANCELED.getTranslated().equals("Cancelado");
        assert OrderStatus.APPROVED.getTranslated().equals("Aprovado");
    }
}