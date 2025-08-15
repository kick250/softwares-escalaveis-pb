package com.erp.server.exceptions;

public class StockAlreadyHasProductException extends Exception {
    public StockAlreadyHasProductException() {
        super("Esse estoque já possui um item com esse produto.");
    }
}
