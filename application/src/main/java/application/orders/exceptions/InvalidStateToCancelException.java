package application.orders.exceptions;

public class InvalidStateToCancelException extends Exception {
    public InvalidStateToCancelException() {
        super("Esse pedido não pode ser cancelado no estado atual.");
    }
}
