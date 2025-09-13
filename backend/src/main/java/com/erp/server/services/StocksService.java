package com.erp.server.services;

import infra.global.entities.StockEntity;
import com.erp.server.exceptions.InvalidStockNameException;
import com.erp.server.exceptions.StockNotFoundException;
import infra.global.repositories.StocksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StocksService {
    private final StocksRepository stocksRepository;

    public List<StockEntity> getAll() {
        return stocksRepository.findAllByDeletedFalse();
    }

    public StockEntity getById(Long id) throws StockNotFoundException {
        return stocksRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(StockNotFoundException::new);
    }

    public void create(String name) throws InvalidStockNameException {
        StockEntity stockEntity = new StockEntity(name);

        if (name.isBlank()) this.throwsInvalidStockNameException();

        stocksRepository.save(stockEntity);
    }

    private void throwsInvalidStockNameException() throws InvalidStockNameException {
        throw new InvalidStockNameException();
    }

    public void update(Long id, String name) throws StockNotFoundException, InvalidStockNameException {
        if (name.isBlank()) this.throwsInvalidStockNameException();

        StockEntity stockEntity = getById(id);

        stockEntity.setName(name);
        stocksRepository.save(stockEntity);
    }

    public void deleteById(Long id) throws StockNotFoundException {
        StockEntity stockEntity = getById(id);
        stockEntity.delete();
        stocksRepository.save(stockEntity);
    }
}
