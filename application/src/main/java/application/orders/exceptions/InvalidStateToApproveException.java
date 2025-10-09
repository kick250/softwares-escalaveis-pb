package application.orders.exceptions;

public class InvalidStateToApproveException extends Exception {
	public InvalidStateToApproveException() {
		super("Pedido não está em um estado válido para aprovação.");
	}
}
