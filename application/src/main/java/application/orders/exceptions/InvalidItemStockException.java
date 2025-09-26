package application.orders.exceptions;

public class InvalidItemStockException extends Exception {
    public InvalidItemStockException() {
        super("Um ou mais itens do pedido estão com estoque inválido");
    }
}
