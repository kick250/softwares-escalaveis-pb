package com.erp.server.controllers;

import com.erp.server.entities.StockItem;
import com.erp.server.exceptions.ProductNotFoundException;
import com.erp.server.exceptions.StockItemNotFoundException;
import com.erp.server.exceptions.StockNotFoundException;
import com.erp.server.requests.StockItemCreateRequest;
import com.erp.server.requests.StockItemUpdateRequest;
import com.erp.server.responses.DefaultErrorResponse;
import com.erp.server.responses.StockItemResponse;
import com.erp.server.services.StockItemsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock_items")
public class StockItemsController {
    private final StockItemsService stockItemsService;

    public StockItemsController(StockItemsService stockItemsService) {
        this.stockItemsService = stockItemsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            StockItem stockItem = stockItemsService.getById(id);
            return ResponseEntity.ok(new StockItemResponse(stockItem));
        } catch (StockItemNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@Valid @RequestBody StockItemCreateRequest request) {
        try {
            stockItemsService.create(request.price(), request.quantity(), request.productId(), request.stockId());
            return ResponseEntity.status(201).body("Item de estoque criado com sucesso.");
        } catch (ProductNotFoundException | StockNotFoundException e) {
            return ResponseEntity.status(404).body(new DefaultErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody StockItemUpdateRequest request) {
        try {
            stockItemsService.update(id, request.price(), request.quantity(), request.productId());
            return ResponseEntity.ok("Item de estoque atualizado com sucesso.");
        } catch (StockItemNotFoundException | ProductNotFoundException e) {
            return ResponseEntity.status(404).body(new DefaultErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            stockItemsService.deleteById(id);
            return ResponseEntity.ok("Item de estoque deletado com sucesso.");
        } catch (StockItemNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
