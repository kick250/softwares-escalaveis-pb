package com.erp.server.exceptions;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException() {
        super("Produto não encontrado");
    }
}
