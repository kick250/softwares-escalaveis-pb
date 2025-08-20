package com.erp.server.factories;

import com.erp.server.entities.Product;
import com.erp.server.entities.StockItem;
import org.instancio.Instancio;
import org.instancio.Select;

public class StockItemsFactory {
    public StockItem createStockItem() {
        return Instancio.of(StockItem.class)
                .set(Select.field("id"), null)
                .set(Select.field("deleted"), false)
                .set(Select.field("price"), 199.99)
                .set(Select.field("quantity"), 100)
                .set(Select.field("product"), null)
                .set(Select.field("stock"), null)
                .create();
    }

    public StockItem createStockItemWithProduct(Product product) {
        return Instancio.of(StockItem.class)
                .set(Select.field("id"), null)
                .set(Select.field("deleted"), false)
                .set(Select.field("price"), 199.99)
                .set(Select.field("quantity"), 100)
                .set(Select.field("product"), product)
                .set(Select.field("stock"), null)
                .create();
    }
}
