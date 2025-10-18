package com.erp.server.controllers.portal;

import com.erp.server.responses.portal.ProductResponse;
import com.erp.server.services.StockItemsService;
import infra.global.relational.entities.StockItemEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("portalProductsController")
@RequestMapping("/portal/products")
@AllArgsConstructor
public class ProductsController {
    private final StockItemsService stockItemsService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(@RequestParam(name="stockId", required=true) Long stockId) {
        List<StockItemEntity> products = stockItemsService.getByStockId(stockId);
        return ResponseEntity.ok().body(products.stream().map(ProductResponse::new).toList());
    }
}
