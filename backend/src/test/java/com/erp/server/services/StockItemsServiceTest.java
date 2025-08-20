package com.erp.server.services;

import com.erp.server.entities.Product;
import com.erp.server.entities.Stock;
import com.erp.server.entities.StockItem;
import com.erp.server.exceptions.*;
import com.erp.server.repositories.ProductsRepository;
import com.erp.server.repositories.StockItemsRepository;
import com.erp.server.repositories.StocksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockItemsServiceTest {
    private StockItemsService stockItemsService;
    private StockItemsRepository stockItemsRepository;
    private ProductsRepository productsRepository;
    private StocksRepository stocksRepository;

    @BeforeEach
    public void setUp() {
        stockItemsRepository = mock(StockItemsRepository.class);
        productsRepository = mock(ProductsRepository.class);
        stocksRepository = mock(StocksRepository.class);

        stockItemsService = new StockItemsService(stockItemsRepository, productsRepository, stocksRepository);
    }

    @Test
    public void testGetById() throws StockItemNotFoundException {
        Long stockItemId = 1L;
        StockItem stockItem = mock(StockItem.class);
        when(stockItemsRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.of(stockItem));

        StockItem result = stockItemsService.getById(stockItemId);
        assertNotNull(result);
        assertEquals(stockItem, result);
    }

    @Test
    public void testGetById_whenNotFound_throwsException() {
        Long stockItemId = 1L;
        when(stockItemsRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.empty());

        assertThrows(StockItemNotFoundException.class, () -> stockItemsService.getById(stockItemId));
    }

    @Test
    public void testCreate() throws StockAlreadyHasProductException, StockNotFoundException, InvalidItemPriceOrQuantityException, ProductNotFoundException {
        double price = 199.99;
        int quantity = 10;
        Long productId = 1L;
        Long stockId = 2L;

        Product product = mock(Product.class);
        Stock stock = mock(Stock.class);

        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(product));
        when(stocksRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.of(stock));
        when(stock.hasItemWithProduct(product)).thenReturn(false);

        stockItemsService.create(price, quantity, productId, stockId);

        verify(stockItemsRepository).save(any(StockItem.class));
    }

    @Test
    public void testCreate_whenProductNotFound_throwsException() {
        double price = 199.99;
        int quantity = 10;
        Long productId = 1L;
        Long stockId = 2L;

        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFoundException.class, () -> stockItemsService.create(price, quantity, productId, stockId));
        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    public void testCreate_whenStockNotFound_throwsException() {
        double price = 199.99;
        int quantity = 10;
        Long productId = 1L;
        Long stockId = 2L;

        Product product = mock(Product.class);

        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(product));
        when(stocksRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(StockNotFoundException.class, () -> stockItemsService.create(price, quantity, productId, stockId));
        assertEquals("Estoque não encontrado", exception.getMessage());
    }

    @Test
    public void testCreate_whenInvalidPrice_throwsException() {
        double price = -199.99;
        int quantity = 10;
        Long productId = 1L;
        Long stockId = 2L;

        Product product = mock(Product.class);
        Stock stock = mock(Stock.class);

        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(product));
        when(stocksRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.of(stock));

        Exception exception = assertThrows(InvalidItemPriceOrQuantityException.class, () -> stockItemsService.create(price, quantity, productId, stockId));
        assertEquals("Preço ou quantidade inválidos", exception.getMessage());
    }

    @Test
    public void testCreate_whenInvalidQuantity_throwsException() {
        double price = 199.99;
        int quantity = -10;
        Long productId = 1L;
        Long stockId = 2L;

        Product product = mock(Product.class);
        Stock stock = mock(Stock.class);

        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(product));
        when(stocksRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.of(stock));

        Exception exception = assertThrows(InvalidItemPriceOrQuantityException.class, () -> stockItemsService.create(price, quantity, productId, stockId));
        assertEquals("Preço ou quantidade inválidos", exception.getMessage());
    }

    @Test
    public void testCreate_whenStockAlreadyHasProduct_throwsException() {
        double price = 199.99;
        int quantity = 10;
        Long productId = 1L;
        Long stockId = 2L;

        Product product = mock(Product.class);
        Stock stock = mock(Stock.class);

        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(product));
        when(stocksRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.of(stock));
        when(stock.hasItemWithProduct(product)).thenReturn(true);

        Exception exception = assertThrows(StockAlreadyHasProductException.class, () -> stockItemsService.create(price, quantity, productId, stockId));
        assertEquals("Esse estoque já possui um item com esse produto.", exception.getMessage());
    }

    @Test
    public void testUpdate() throws StockItemNotFoundException, InvalidItemPriceOrQuantityException {
        Long stockItemId = 1L;
        double newPrice = 150.00;
        int newQuantity = 20;

        StockItem stockItem = mock(StockItem.class);

        when(stockItemsRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.of(stockItem));

        stockItemsService.update(stockItemId, newPrice, newQuantity);

        verify(stockItem).setPrice(newPrice);
        verify(stockItem).setQuantity(newQuantity);
        verify(stockItemsRepository).save(stockItem);
    }

    @Test
    public void testUpdate_whenStockItemNotFound_throwsException() {
        Long stockItemId = 1L;
        double newPrice = 150.00;
        int newQuantity = 20;

        when(stockItemsRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(StockItemNotFoundException.class, () -> stockItemsService.update(stockItemId, newPrice, newQuantity));
        assertEquals("Item de estoque não encontrado", exception.getMessage());
    }

    @Test
    public void testUpdate_whenInvalidPrice_throwsException() {
        Long stockItemId = 1L;
        double newPrice = -150.00;
        int newQuantity = 20;

        when(stockItemsRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.of(mock(StockItem.class)));

        Exception exception = assertThrows(InvalidItemPriceOrQuantityException.class, () -> stockItemsService.update(stockItemId, newPrice, newQuantity));
        assertEquals("Preço ou quantidade inválidos", exception.getMessage());
    }

    @Test
    public void testUpdate_whenInvalidQuantity_throwsException() {
        Long stockItemId = 1L;
        double newPrice = 150.00;
        int newQuantity = -20;

        when(stockItemsRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.of(mock(StockItem.class)));

        Exception exception = assertThrows(InvalidItemPriceOrQuantityException.class, () -> stockItemsService.update(stockItemId, newPrice, newQuantity));
        assertEquals("Preço ou quantidade inválidos", exception.getMessage());
    }

    @Test
    public void testDeleteById() throws StockItemNotFoundException {
        Long stockItemId = 1L;

        StockItem stockItem = mock(StockItem.class);

        when(stockItemsRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.of(stockItem));

        stockItemsService.deleteById(stockItemId);

        verify(stockItem).delete();
        verify(stockItemsRepository).save(stockItem);
    }

    @Test
    public void testDeleteById_whenStockItemNotFound_throwsException() {
        Long stockItemId = 1L;

        when(stockItemsRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.empty());

        assertThrows(StockItemNotFoundException.class, () -> stockItemsService.deleteById(stockItemId));
    }
}