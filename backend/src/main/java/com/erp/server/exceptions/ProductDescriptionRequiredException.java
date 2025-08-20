package com.erp.server.exceptions;

public class ProductDescriptionRequiredException extends Exception {
    public ProductDescriptionRequiredException() {
        super("A descrição do produto é obrigatória");
    }
}
