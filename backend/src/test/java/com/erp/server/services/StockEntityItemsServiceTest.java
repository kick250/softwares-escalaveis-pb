package com.erp.server.services;

import infra.global.entities.ProductEntity;
import infra.global.entities.StockEntity;
import infra.global.entities.StockItemEntity;
import com.erp.server.exceptions.*;
import infra.global.repositories.ProductsRepository;
import infra.global.repositories.StockItemsRepository;
import infra.global.repositories.StocksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockEntityItemsServiceTest {
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
        StockItemEntity stockItem = mock(StockItemEntity.class);
        when(stockItemsRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.of(stockItem));

        StockItemEntity result = stockItemsService.getById(stockItemId);
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

        ProductEntity productEntity = mock(ProductEntity.class);
        StockEntity stockEntity = mock(StockEntity.class);

        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(productEntity));
        when(stocksRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.of(stockEntity));
        when(stockEntity.hasItemWithProduct(productEntity)).thenReturn(false);

        stockItemsService.create(price, quantity, productId, stockId);

        verify(stockItemsRepository).save(any(StockItemEntity.class));
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

        ProductEntity productEntity = mock(ProductEntity.class);
        StockEntity stockEntity = mock(StockEntity.class);

        when(productsRepository.findByIdAndDeletedFalse(productId)).thenReturn(Optional.of(productEntity));
        when(stocksRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.of(stockEntity));

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
        when(stocksRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.of(stockEntity));

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
        when(stocksRepository.findByIdAndDeletedFalse(stockId)).thenReturn(Optional.of(stockEntity));
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

        when(stockItemsRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.of(mock(StockItemEntity.class)));

        Exception exception = assertThrows(InvalidItemPriceOrQuantityException.class, () -> stockItemsService.update(stockItemId, newPrice, newQuantity));
        assertEquals("Preço ou quantidade inválidos", exception.getMessage());
    }

    @Test
    public void testUpdate_whenInvalidQuantity_throwsException() {
        Long stockItemId = 1L;
        double newPrice = 150.00;
        int newQuantity = -20;

        when(stockItemsRepository.findByIdAndDeletedFalse(stockItemId)).thenReturn(Optional.of(mock(StockItemEntity.class)));

        Exception exception = assertThrows(InvalidItemPriceOrQuantityException.class, () -> stockItemsService.update(stockItemId, newPrice, newQuantity));
        assertEquals("Preço ou quantidade inválidos", exception.getMessage());
    }

    @Test
    public void testDeleteById() throws StockItemNotFoundException {
        Long stockItemId = 1L;

        StockItemEntity stockItem = mock(StockItemEntity.class);

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