package com.erp.server.exceptions;

public class ProductImageRequiredException extends Exception {
    public ProductImageRequiredException() {
        super("A imagem do produto é obrigatória");
    }
}
