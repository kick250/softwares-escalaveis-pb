package com.erp.server.services;

import infra.global.entities.ProductEntity;
import infra.global.entities.StockEntity;
import infra.global.entities.StockItemEntity;
import com.erp.server.exceptions.*;
import infra.global.repositories.ProductsRepository;
import infra.global.repositories.StockItemsRepository;
import infra.global.repositories.StocksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StockItemsService {
    private final StockItemsRepository stockItemsRepository;
    private final ProductsRepository productsRepository;
    private final StocksRepository stocksRepository;

    public StockItemEntity getById(Long id) throws StockItemNotFoundException {
        return stockItemsRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(StockItemNotFoundException::new);
    }

    public void create(double price, int quantity, Long productId, Long stockId) throws ProductNotFoundException, StockNotFoundException, StockAlreadyHasProductException, InvalidItemPriceOrQuantityException {
        ProductEntity productEntity = getProductById(productId);
        StockEntity stockEntity = getStockById(stockId);

        if (price <= 0 || quantity <= 0) this.throwInvalidPriceOrQuantityException();
        if (stockEntity.hasItemWithProduct(productEntity)) this.throwStockAlreadyHasProductException();

        StockItemEntity stockItem = new StockItemEntity(price, quantity, productEntity, stockEntity);
        stockItemsRepository.save(stockItem);
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
        return stocksRepository.findByIdAndDeletedFalse(stockId)
                .orElseThrow(StockNotFoundException::new);
    }

    public void update(Long id, double price, int quantity) throws StockItemNotFoundException, InvalidItemPriceOrQuantityException {
        StockItemEntity stockItem = getById(id);

        if (price <= 0 || quantity <= 0) this.throwInvalidPriceOrQuantityException();

        stockItem.setPrice(price);
        stockItem.setQuantity(quantity);
        stockItemsRepository.save(stockItem);
    }

    public void deleteById(Long id) throws StockItemNotFoundException {
        StockItemEntity stockItem = getById(id);
        stockItem.delete();
        stockItemsRepository.save(stockItem);
    }
}
