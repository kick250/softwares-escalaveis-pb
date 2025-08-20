package com.erp.server.exceptions;

public class ProductNameRequiredException extends Exception {
    public ProductNameRequiredException() {
        super("O nome do produto é obrigatório");
    }
}
