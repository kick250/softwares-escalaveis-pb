package com.erp.server.services;

import com.erp.server.entities.Stock;
import com.erp.server.exceptions.StockNotFoundException;
import com.erp.server.repositories.StocksRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StocksService {
    private final StocksRepository stocksRepository;

    public StocksService(StocksRepository stocksRepository) {
        this.stocksRepository = stocksRepository;
    }

    public List<Stock> getAll() {
        return stocksRepository.findAllByDeletedFalse();
    }

    public Stock getById(Long id) throws StockNotFoundException {
        return stocksRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(StockNotFoundException::new);
    }

    public void create(String name) {
        Stock stock = new Stock(name);
        stocksRepository.save(stock);
    }

    public void update(Long id, String name) throws StockNotFoundException {
        Stock stock = getById(id);
        stock.setName(name);
        stocksRepository.save(stock);
    }

    public void deleteById(Long id) throws StockNotFoundException {
        Stock stock = getById(id);
        stock.delete();
        stocksRepository.save(stock);
    }
}
