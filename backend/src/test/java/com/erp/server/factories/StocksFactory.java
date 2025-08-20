package com.erp.server.factories;

import com.erp.server.entities.Stock;
import org.instancio.Instancio;
import org.instancio.Select;

import java.util.ArrayList;

public class StocksFactory {

    public Stock createStock() {
        return Instancio.of(Stock.class)
                .set(Select.field("id"), null)
                .set(Select.field("deleted"), false)
                .set(Select.field("name"), "Test Stock")
                .set(Select.field("stockItems"), new ArrayList<>())
                .create();
    }
}
