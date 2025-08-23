package application.orders.exceptions;

public class OrderOwnerNotFoundException extends Exception {
	public OrderOwnerNotFoundException() {
		super("O dono do pedido não foi encontrado");
	}
}
