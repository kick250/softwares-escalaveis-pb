package com.erp.server.controllers;

import com.erp.server.entities.Stock;
import com.erp.server.exceptions.StockNotFoundException;
import com.erp.server.requests.StockCreateRequest;
import com.erp.server.requests.StockUpdateRequest;
import com.erp.server.responses.StockResponse;
import com.erp.server.responses.StocksResponse;
import com.erp.server.services.StocksService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StocksController {
    private final StocksService stocksService;

    public StocksController(StocksService stocksService) {
        this.stocksService = stocksService;
    }

    @GetMapping
    public ResponseEntity<StocksResponse> getStocks() {
        List<Stock> stocks = stocksService.getAll();
        return ResponseEntity.ok(new StocksResponse(stocks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> getById(@PathVariable("id") Long id) {
        try {
            Stock stock = stocksService.getById(id);
            return ResponseEntity.status(201).body(new StockResponse(stock));
        } catch (StockNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> create(@Valid @RequestBody StockCreateRequest request) {
        stocksService.create(request.name());
        return ResponseEntity.ok("Estoque criado com sucesso");
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> update(@PathVariable("id") Long id, @Valid @RequestBody StockUpdateRequest request) {
        try {
            stocksService.update(id, request.name());
            return ResponseEntity.ok("Estoque atualizado com sucesso");
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
