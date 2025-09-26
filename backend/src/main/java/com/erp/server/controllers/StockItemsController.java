package com.erp.server.controllers;

import infra.global.entities.StockItemEntity;
import com.erp.server.exceptions.*;
import com.erp.server.requests.StockItemCreateRequest;
import com.erp.server.requests.StockItemUpdateRequest;
import com.erp.server.responses.DefaultErrorResponse;
import com.erp.server.responses.StockItemResponse;
import com.erp.server.services.StockItemsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock_items")
@AllArgsConstructor
public class StockItemsController {
    private final StockItemsService stockItemsService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            StockItemEntity stockItem = stockItemsService.getById(id);
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
        } catch (StockAlreadyHasProductException | InvalidItemPriceOrQuantityException e) {
            return ResponseEntity.status(400).body(new DefaultErrorResponse(e.getMessage()));
        } catch (ProductNotFoundException | StockNotFoundException e) {
            return ResponseEntity.status(404).body(new DefaultErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody StockItemUpdateRequest request) {
        try {
            stockItemsService.update(id, request.price(), request.quantity());
            return ResponseEntity.ok("Item de estoque atualizado com sucesso.");
        } catch (InvalidItemPriceOrQuantityException e) {
            return ResponseEntity.status(400).body(new DefaultErrorResponse(e.getMessage()));
        } catch (StockItemNotFoundException e) {
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
