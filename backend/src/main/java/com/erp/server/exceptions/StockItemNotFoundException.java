package com.erp.server.exceptions;

public class StockItemNotFoundException extends Exception {
    public StockItemNotFoundException() {
        super("Item de estoque não encontrado.");
    }
}
