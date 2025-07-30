package com.erp.server.exceptions;

public class StockNotFoundException extends Exception{
    public StockNotFoundException() {
        super("Estoque não encontrado");
    }
}
