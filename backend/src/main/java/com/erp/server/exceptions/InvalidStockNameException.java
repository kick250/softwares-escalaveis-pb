package com.erp.server.exceptions;

public class InvalidStockNameException extends Exception {
    public InvalidStockNameException() {
        super("Um estoque precisa ter um nome válido");
    }
}
