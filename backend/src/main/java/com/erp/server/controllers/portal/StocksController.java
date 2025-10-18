package com.erp.server.controllers.portal;

import com.erp.server.responses.portal.StockResponse;
import com.erp.server.services.StocksService;
import infra.global.relational.entities.StockEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("portalStocksController")
@RequestMapping("/portal/stocks")
@AllArgsConstructor
public class StocksController {
    private final StocksService stocksService;

    @GetMapping
    public ResponseEntity<List<StockResponse>> getAll() {
        List<StockEntity> stocks = stocksService.getAll();
        return ResponseEntity.ok(stocks.stream().map(StockResponse::new).toList());
    }
}
