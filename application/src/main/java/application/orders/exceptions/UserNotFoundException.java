package application.orders.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("Usuário não encontrado");
    }
}
