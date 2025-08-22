package com.erp.server.factories;

import infra.global.entities.StockEntity;
import org.instancio.Instancio;
import org.instancio.Select;

import java.util.ArrayList;

public class StocksFactory {

    public StockEntity createStock() {
        return Instancio.of(StockEntity.class)
                .set(Select.field("id"), null)
                .set(Select.field("deleted"), false)
                .set(Select.field("name"), "Test Stock")
                .set(Select.field("stockItems"), new ArrayList<>())
                .create();
    }
}
