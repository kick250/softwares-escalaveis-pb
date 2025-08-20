package com.erp.server.exceptions;

public class InvalidItemPriceOrQuantityException extends Exception {
    public InvalidItemPriceOrQuantityException() {
        super("Preço ou quantidade inválidos");
    }
}
