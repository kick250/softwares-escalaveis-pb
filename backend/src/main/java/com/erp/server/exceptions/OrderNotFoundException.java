package com.erp.server.exceptions;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException() {
        super("Pedido não encontrado.");
    }
}
