package com.erp.server.services;

import com.erp.server.entities.Stock;
import com.erp.server.exceptions.InvalidStockNameException;
import com.erp.server.exceptions.StockNotFoundException;
import com.erp.server.repositories.StocksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StocksServiceTest {
    private StocksService stocksService;
    private StocksRepository stocksRepository;

    @BeforeEach
    public void setUp() {
        stocksRepository = mock(StocksRepository.class);

        stocksService = new StocksService(stocksRepository);
    }

    @Test
    public void testGetAll() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(mock(Stock.class));
        stocks.add(mock(Stock.class));
        stocks.add(mock(Stock.class));

        when(stocksRepository.findAllByDeletedFalse()).thenReturn(stocks);

        List<Stock> result = stocksService.getAll();

        assertEquals(stocks, result);
    }

    @Test
    public void testGetById() throws StockNotFoundException {
        Long id = 1L;

        Stock stock = mock(Stock.class);

        when(stocksRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.of(stock));

        Stock result = stocksService.getById(id);

        assertEquals(stock, result);
    }

    @Test
    public void testGetById_whenNotFound_throwsException() {
        Long id = 1L;

        when(stocksRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(StockNotFoundException.class, () -> stocksService.getById(id));
        assertEquals("Estoque não encontrado", exception.getMessage());
    }

    @Test
    public void testCreate() throws InvalidStockNameException {
        String name = "New Stock";

        stocksService.create(name);

        verify(stocksRepository).save(any(Stock.class));
    }

    @Test
    public void testCreate_whenNameIsBlank_throwsException() {
        String name = "";

        Exception exception = assertThrows(InvalidStockNameException.class, () -> stocksService.create(name));
        assertEquals("Um estoque precisa ter um nome válido", exception.getMessage());
    }

    @Test
    public void testUpdate() throws StockNotFoundException, InvalidStockNameException {
        Long id = 1L;
        String name = "New name";

        Stock stock = mock(Stock.class);

        when(stocksRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.of(stock));

        stocksService.update(id, name);

        verify(stock).setName(name);
        verify(stocksRepository).save(any(Stock.class));
    }

    @Test
    public void testUpdate_whenNotFound_throwsException() {
        Long id = 1L;
        String name = "New name";

        when(stocksRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(StockNotFoundException.class, () -> stocksService.update(id, name));
        assertEquals("Estoque não encontrado", exception.getMessage());
    }

    @Test
    public void testUpdate_whenNameIsBlank_throwsException() {
        Long id = 1L;
        String name = "";

        Exception exception = assertThrows(InvalidStockNameException.class, () -> stocksService.update(id, name));
        assertEquals("Um estoque precisa ter um nome válido", exception.getMessage());
    }

    @Test
    public void testDeleteById() throws StockNotFoundException {
        Long id = 1L;

        Stock stock = mock(Stock.class);

        when(stocksRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.of(stock));

        stocksService.deleteById(id);

        verify(stock).delete();
        verify(stocksRepository).save(any(Stock.class));
    }

    @Test
    public void testDeleteById_whenNotFound_throwsException() {
        Long id = 1L;

        when(stocksRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(StockNotFoundException.class, () -> stocksService.deleteById(id));
        assertEquals("Estoque não encontrado", exception.getMessage());
    }
}