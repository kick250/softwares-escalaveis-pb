package com.erp.server.exceptions;

public class CartNotFoundException extends Exception {
    public CartNotFoundException() {
        super("Não foi possível encontrar o carrinho especificado.");
    }
}
