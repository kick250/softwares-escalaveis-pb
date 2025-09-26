package application.orders.exceptions;

import application.orders.domain.Item;

public class UnavailableItemQuantityException extends Exception {
    public UnavailableItemQuantityException(Item item) {
        super("O stock não possui a quantidade disponível do item: " + item.getName());
    }
}
