package com.erp.server.factories;
import infra.global.entities.ProductEntity;
import org.instancio.Instancio;
import org.instancio.Select;

import java.util.ArrayList;

public class ProductsFactory {
    public ProductEntity createProduct() {
        return Instancio.of(ProductEntity.class)
                .set(Select.field("id"), null)
                .set(Select.field("deleted"), false)
                .set(Select.field("name"), "Produto de Teste")
                .set(Select.field("description"), "Descrição do Produto de Teste")
                .set(Select.field("stockItems"), new ArrayList<>())
                .create();
    }
}
