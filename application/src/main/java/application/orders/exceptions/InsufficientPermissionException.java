package application.orders.exceptions;

public class InsufficientPermissionException extends Exception {
    public InsufficientPermissionException() {
        super("O usuário não tem permissão para realizar esta ação.");
    }
}
