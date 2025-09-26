package application.orders.exceptions;

public class SomeItemsWereNotFoundException extends Exception {
    public SomeItemsWereNotFoundException() {
        super("Alguns itens do pedido não foram encontrados");
    }
}
