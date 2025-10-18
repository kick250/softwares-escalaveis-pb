package com.erp.server.services;

import infra.global.relational.entities.StockEntity;
import com.erp.server.exceptions.InvalidStockNameException;
import com.erp.server.exceptions.StockNotFoundException;
import infra.global.relational.repositories.StocksJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StocksService {
    private final StocksJpaRepository stocksJpaRepository;

    public List<StockEntity> getAll() {
        return stocksJpaRepository.findAllByDeletedFalse();
    }

    public StockEntity getById(Long id) throws StockNotFoundException {
        return stocksJpaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(StockNotFoundException::new);
    }

    public void create(String name) throws InvalidStockNameException {
        StockEntity stockEntity = new StockEntity(name);

        if (name.isBlank()) this.throwsInvalidStockNameException();

        stocksJpaRepository.save(stockEntity);
    }

    private void throwsInvalidStockNameException() throws InvalidStockNameException {
        throw new InvalidStockNameException();
    }

    public void update(Long id, String name) throws StockNotFoundException, InvalidStockNameException {
        if (name.isBlank()) this.throwsInvalidStockNameException();

        StockEntity stockEntity = getById(id);

        stockEntity.setName(name);
        stocksJpaRepository.save(stockEntity);
    }

    public void deleteById(Long id) throws StockNotFoundException {
        StockEntity stockEntity = getById(id);
        stockEntity.delete();
        stocksJpaRepository.save(stockEntity);
    }
}
