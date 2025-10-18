package com.erp.server.controllers;

import infra.global.relational.entities.StockEntity;
import com.erp.server.exceptions.InvalidStockNameException;
import com.erp.server.exceptions.StockNotFoundException;
import com.erp.server.requests.StockCreateRequest;
import com.erp.server.requests.StockUpdateRequest;
import com.erp.server.responses.DefaultErrorResponse;
import com.erp.server.responses.StockResponse;
import com.erp.server.responses.StocksResponse;
import com.erp.server.services.StocksService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@AllArgsConstructor
public class StocksController {
    private final StocksService stocksService;

    @GetMapping
    public ResponseEntity<StocksResponse> getStocks() {
        List<StockEntity> stockEntities = stocksService.getAll();
        return ResponseEntity.ok(new StocksResponse(stockEntities));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> getById(@PathVariable("id") Long id) {
        try {
            StockEntity stockEntity = stocksService.getById(id);
            return ResponseEntity.ok().body(new StockResponse(stockEntity));
        } catch (StockNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> create(@Valid @RequestBody StockCreateRequest request) {
        try {
            stocksService.create(request.name());
            return ResponseEntity.status(201).body("Estoque criado com sucesso");
        } catch (InvalidStockNameException e) {
            return ResponseEntity.badRequest().body(new DefaultErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid @RequestBody StockUpdateRequest request) {
        try {
            stocksService.update(id, request.name());
            return ResponseEntity.ok("Estoque atualizado com sucesso");
        } catch (InvalidStockNameException e) {
            return ResponseEntity.badRequest().body(new DefaultErrorResponse(e.getMessage()));
        } catch (StockNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        try {
            stocksService.deleteById(id);
            return ResponseEntity.ok("Estoque deletado com sucesso");
        } catch (StockNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
