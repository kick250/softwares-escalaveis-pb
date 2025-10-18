package com.erp.server.services;

import infra.global.relational.entities.ProductEntity;
import infra.global.relational.entities.StockEntity;
import infra.global.relational.entities.StockItemEntity;
import com.erp.server.exceptions.*;
import infra.global.relational.repositories.ProductsJpaRepository;
import infra.global.relational.repositories.StockItemsJpaRepository;
import infra.global.relational.repositories.StocksJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StockItemsService {
    private final StockItemsJpaRepository stockItemsJpaRepository;
    private final ProductsJpaRepository productsRepository;
    private final StocksJpaRepository stocksJpaRepository;

    public StockItemEntity getById(Long id) throws StockItemNotFoundException {
        return stockItemsJpaRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(StockItemNotFoundException::new);
    }

    public List<StockItemEntity> getByStockId(Long stockId) {
        return stockItemsJpaRepository.findAllByStockIdAndDeletedFalse(stockId);
    }

    public void create(double price, int quantity, Long productId, Long stockId) throws ProductNotFoundException, StockNotFoundException, StockAlreadyHasProductException, InvalidItemPriceOrQuantityException {
        ProductEntity productEntity = getProductById(productId);
        StockEntity stockEntity = getStockById(stockId);

        if (price <= 0 || quantity <= 0) this.throwInvalidPriceOrQuantityException();
        if (stockEntity.hasItemWithProduct(productEntity)) this.throwStockAlreadyHasProductException();

        StockItemEntity stockItem = new StockItemEntity(price, quantity, productEntity, stockEntity);
        stockItemsJpaRepository.save(stockItem);
    }

    private void throwInvalidPriceOrQuantityException() throws InvalidItemPriceOrQuantityException {
        throw new InvalidItemPriceOrQuantityException();
    }

    private void throwStockAlreadyHasProductException() throws StockAlreadyHasProductException {
        throw new StockAlreadyHasProductException();
    }

    private ProductEntity getProductById(Long productId) throws ProductNotFoundException {
        return productsRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    private StockEntity getStockById(Long stockId) throws StockNotFoundException {
        return stocksJpaRepository.findByIdAndDeletedFalse(stockId)
                .orElseThrow(StockNotFoundException::new);
    }

    public void update(Long id, double price, int quantity) throws StockItemNotFoundException, InvalidItemPriceOrQuantityException {
        StockItemEntity stockItem = getById(id);

        if (price <= 0 || quantity <= 0) this.throwInvalidPriceOrQuantityException();

        stockItem.setPrice(price);
        stockItem.setQuantity(quantity);
        stockItemsJpaRepository.save(stockItem);
    }

    public void deleteById(Long id) throws StockItemNotFoundException {
        StockItemEntity stockItem = getById(id);
        stockItem.delete();
        stockItemsJpaRepository.save(stockItem);
    }
}
