package com.erp.server.services;

import com.erp.server.entities.Product;
import com.erp.server.entities.Stock;
import com.erp.server.entities.StockItem;
import com.erp.server.exceptions.ProductNotFoundException;
import com.erp.server.exceptions.StockItemNotFoundException;
import com.erp.server.exceptions.StockNotFoundException;
import com.erp.server.repositories.ProductsRepository;
import com.erp.server.repositories.StockItemsRepository;
import com.erp.server.repositories.StocksRepository;
import org.springframework.stereotype.Service;

@Service
public class StockItemsService {
    private final StockItemsRepository stockItemsRepository;
    private final ProductsRepository productsRepository;
    private final StocksRepository stocksRepository;

    public StockItemsService(StockItemsRepository stockItemsRepository, ProductsRepository productsRepository, StocksRepository stocksRepository) {
        this.stockItemsRepository = stockItemsRepository;
        this.productsRepository = productsRepository;
        this.stocksRepository = stocksRepository;
    }

    public StockItem getById(Long id) throws StockItemNotFoundException {
        return stockItemsRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(StockItemNotFoundException::new);
    }

    public void create(double price, int quantity, Long productId, Long stockId) throws ProductNotFoundException, StockNotFoundException {
        Product product = getProductById(productId);
        Stock stock = getStockById(stockId);
        StockItem stockItem = new StockItem(price, quantity, product, stock);
        stockItemsRepository.save(stockItem);
    }

    private Product getProductById(Long productId) throws ProductNotFoundException {
        return productsRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    private Stock getStockById(Long stockId) throws StockNotFoundException {
        return stocksRepository.findByIdAndDeletedFalse(stockId)
                .orElseThrow(StockNotFoundException::new);
    }

    public void update(Long id, double price, int quantity, Long productId) throws StockItemNotFoundException, ProductNotFoundException {
        StockItem stockItem = getById(id);
        Product product = getProductById(productId);
        stockItem.setPrice(price);
        stockItem.setQuantity(quantity);
        stockItem.setProduct(product);
        stockItemsRepository.save(stockItem);
    }

    public void deleteById(Long id) throws StockItemNotFoundException {
        StockItem stockItem = getById(id);
        stockItem.delete();
        stockItemsRepository.save(stockItem);
    }
}
