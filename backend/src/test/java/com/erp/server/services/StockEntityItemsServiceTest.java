package com.erp.server.services;

import infra.global.relational.entities.ProductEntity;
import infra.global.relational.entities.StockEntity;
import infra.global.relational.entities.StockItemEntity;
import com.erp.server.exceptions.*;
import infra.global.relational.repositories.ProductsJpaRepository;
import infra.global.relational.repositories.StockItemsJpaRepository;
import infra.global.relational.repositories.StocksJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockEntityItemsServiceTest {
    private StockItemsService stockItemsService;
    private StockItemsJpaRepository stockItemsJpaRepository;
    private ProductsJpaRepository productsRepository;
    private StocksJpaRepository stocksJpaRepository;

    @BeforeEach
    public void setUp() {
        stockItemsJpaRepository = mock(StockItemsJpaRepository.class);
        productsRepository = mock(ProductsJpaRepository.class);
        stocksJpaRepository = mock(StocksJpaRepository.class);

        stockItemsService = new StockItemsService(stockItemsJpaRepository, productsRepository, stocksJpaRepository);
    }

    @Test
    public void testGetById() throws StockItemNotFoundException {
        Long stockItemId = 1L;
        StockItemEntity stockItem = mock(StockItemEntity.class);
        when(stockItemsJpaRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.of(stockItem));

        StockItemEntity result = stockItemsService.getById(stockItemId);
        assertNotNull(result);
        assertEquals(stockItem, result);
    }

    @Test
    public void testGetById_whenNotFound_throwsException() {
        Long stockItemId = 1L;
        when(stockItemsJpaRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.empty());

        assertThrows(StockItemNotFoundException.class, () -> stockItemsService.getById(stockItemId));
    }

    @Test
    public void testCreate() throws StockAlreadyHasProductException, StockNotFoundException, InvalidItemPriceOrQuantityException, ProductNotFoundException {
        double price = 199.99;
        int quantity = 10;
        Long productId = 1L;
        Long stockId = 2L;

        ProductEntity productEntity = mock(ProductEntity.class);
        StockEntity stockEntity = mock(StockEntity.class);

        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(productEntity));
        when(stocksJpaRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.of(stockEntity));
        when(stockEntity.hasItemWithProduct(productEntity)).thenReturn(false);

        stockItemsService.create(price, quantity, productId, stockId);

        verify(stockItemsJpaRepository).save(any(StockItemEntity.class));
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

        ProductEntity productEntity = mock(ProductEntity.class);

        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(productEntity));
        when(stocksJpaRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(StockNotFoundException.class, () -> stockItemsService.create(price, quantity, productId, stockId));
        assertEquals("Estoque não encontrado", exception.getMessage());
    }

    @Test
    public void testCreate_whenInvalidPrice_throwsException() {
        double price = -199.99;
        int quantity = 10;
        Long productId = 1L;
        Long stockId = 2L;

        ProductEntity productEntity = mock(ProductEntity.class);
        StockEntity stockEntity = mock(StockEntity.class);

        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(productEntity));
        when(stocksJpaRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.of(stockEntity));

        Exception exception = assertThrows(InvalidItemPriceOrQuantityException.class, () -> stockItemsService.create(price, quantity, productId, stockId));
        assertEquals("Preço ou quantidade inválidos", exception.getMessage());
    }

    @Test
    public void testCreate_whenInvalidQuantity_throwsException() {
        double price = 199.99;
        int quantity = -10;
        Long productId = 1L;
        Long stockId = 2L;

        ProductEntity productEntity = mock(ProductEntity.class);
        StockEntity stockEntity = mock(StockEntity.class);

        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(productEntity));
        when(stocksJpaRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.of(stockEntity));

        Exception exception = assertThrows(InvalidItemPriceOrQuantityException.class, () -> stockItemsService.create(price, quantity, productId, stockId));
        assertEquals("Preço ou quantidade inválidos", exception.getMessage());
    }

    @Test
    public void testCreate_whenStockAlreadyHasProduct_throwsException() {
        double price = 199.99;
        int quantity = 10;
        Long productId = 1L;
        Long stockId = 2L;

        ProductEntity productEntity = mock(ProductEntity.class);
        StockEntity stockEntity = mock(StockEntity.class);

        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(productEntity));
        when(stocksJpaRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.of(stockEntity));
        when(stockEntity.hasItemWithProduct(productEntity)).thenReturn(true);

        Exception exception = assertThrows(StockAlreadyHasProductException.class, () -> stockItemsService.create(price, quantity, productId, stockId));
        assertEquals("Esse estoque já possui um item com esse produto.", exception.getMessage());
    }

    @Test
    public void testUpdate() throws StockItemNotFoundException, InvalidItemPriceOrQuantityException {
        Long stockItemId = 1L;
        double newPrice = 150.00;
        int newQuantity = 20;

        StockItemEntity stockItem = mock(StockItemEntity.class);

        when(stockItemsJpaRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.of(stockItem));

        stockItemsService.update(stockItemId, newPrice, newQuantity);

        verify(stockItem).setPrice(newPrice);
        verify(stockItem).setQuantity(newQuantity);
        verify(stockItemsJpaRepository).save(stockItem);
    }

    @Test
    public void testUpdate_whenStockItemNotFound_throwsException() {
        Long stockItemId = 1L;
        double newPrice = 150.00;
        int newQuantity = 20;

        when(stockItemsJpaRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(StockItemNotFoundException.class, () -> stockItemsService.update(stockItemId, newPrice, newQuantity));
        assertEquals("Item de estoque não encontrado", exception.getMessage());
    }

    @Test
    public void testUpdate_whenInvalidPrice_throwsException() {
        Long stockItemId = 1L;
        double newPrice = -150.00;
        int newQuantity = 20;

        when(stockItemsJpaRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.of(mock(StockItemEntity.class)));

        Exception exception = assertThrows(InvalidItemPriceOrQuantityException.class, () -> stockItemsService.update(stockItemId, newPrice, newQuantity));
        assertEquals("Preço ou quantidade inválidos", exception.getMessage());
    }

    @Test
    public void testUpdate_whenInvalidQuantity_throwsException() {
        Long stockItemId = 1L;
        double newPrice = 150.00;
        int newQuantity = -20;

        when(stockItemsJpaRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.of(mock(StockItemEntity.class)));

        Exception exception = assertThrows(InvalidItemPriceOrQuantityException.class, () -> stockItemsService.update(stockItemId, newPrice, newQuantity));
        assertEquals("Preço ou quantidade inválidos", exception.getMessage());
    }

    @Test
    public void testDeleteById() throws StockItemNotFoundException {
        Long stockItemId = 1L;

        StockItemEntity stockItem = mock(StockItemEntity.class);

        when(stockItemsJpaRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.of(stockItem));

        stockItemsService.deleteById(stockItemId);

        verify(stockItem).delete();
        verify(stockItemsJpaRepository).save(stockItem);
    }

    @Test
    public void testDeleteById_whenStockItemNotFound_throwsException() {
        Long stockItemId = 1L;

        when(stockItemsJpaRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.empty());

        assertThrows(StockItemNotFoundException.class, () -> stockItemsService.deleteById(stockItemId));
    }
}