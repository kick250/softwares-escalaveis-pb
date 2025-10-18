package com.erp.server.factories;

import infra.global.relational.entities.ProductEntity;
import infra.global.relational.entities.StockItemEntity;
import org.instancio.Instancio;
import org.instancio.Select;

public class StockItemsFactory {
    public StockItemEntity createStockItem() {
        return Instancio.of(StockItemEntity.class)
                .set(Select.field("id"), null)
                .set(Select.field("deleted"), false)
                .set(Select.field("price"), 199.99)
                .set(Select.field("quantity"), 100)
                .set(Select.field("product"), null)
                .set(Select.field("stock"), null)
                .create();
    }

    public StockItemEntity createStockItemWithProduct(ProductEntity productEntity) {
        return Instancio.of(StockItemEntity.class)
                .set(Select.field("id"), null)
                .set(Select.field("deleted"), false)
                .set(Select.field("price"), 199.99)
                .set(Select.field("quantity"), 100)
                .set(Select.field("product"), productEntity)
                .set(Select.field("stock"), null)
                .create();
    }
}
