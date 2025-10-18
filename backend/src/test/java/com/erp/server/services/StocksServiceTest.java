package com.erp.server.services;

import infra.global.relational.entities.StockEntity;
import com.erp.server.exceptions.InvalidStockNameException;
import com.erp.server.exceptions.StockNotFoundException;
import infra.global.relational.repositories.StocksJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StocksServiceTest {
    private StocksService stocksService;
    private StocksJpaRepository stocksJpaRepository;

    @BeforeEach
    public void setUp() {
        stocksJpaRepository = mock(StocksJpaRepository.class);

        stocksService = new StocksService(stocksJpaRepository);
    }

    @Test
    public void testGetAll() {
        List<StockEntity> stockEntities = new ArrayList<>();
        stockEntities.add(mock(StockEntity.class));
        stockEntities.add(mock(StockEntity.class));
        stockEntities.add(mock(StockEntity.class));

        when(stocksJpaRepository.findAllByDeletedFalse()).thenReturn(stockEntities);

        List<StockEntity> result = stocksService.getAll();

        assertEquals(stockEntities, result);
    }

    @Test
    public void testGetById() throws StockNotFoundException {
        Long id = 1L;

        StockEntity stockEntity = mock(StockEntity.class);

        when(stocksJpaRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.of(stockEntity));

        StockEntity result = stocksService.getById(id);

        assertEquals(stockEntity, result);
    }

    @Test
    public void testGetById_whenNotFound_throwsException() {
        Long id = 1L;

        when(stocksJpaRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(StockNotFoundException.class, () -> stocksService.getById(id));
        assertEquals("Estoque não encontrado", exception.getMessage());
    }

    @Test
    public void testCreate() throws InvalidStockNameException {
        String name = "New Stock";

        stocksService.create(name);

        verify(stocksJpaRepository).save(any(StockEntity.class));
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

        StockEntity stockEntity = mock(StockEntity.class);

        when(stocksJpaRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.of(stockEntity));

        stocksService.update(id, name);

        verify(stockEntity).setName(name);
        verify(stocksJpaRepository).save(any(StockEntity.class));
    }

    @Test
    public void testUpdate_whenNotFound_throwsException() {
        Long id = 1L;
        String name = "New name";

        when(stocksJpaRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.empty());

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

        StockEntity stockEntity = mock(StockEntity.class);

        when(stocksJpaRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.of(stockEntity));

        stocksService.deleteById(id);

        verify(stockEntity).delete();
        verify(stocksJpaRepository).save(any(StockEntity.class));
    }

    @Test
    public void testDeleteById_whenNotFound_throwsException() {
        Long id = 1L;

        when(stocksJpaRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(StockNotFoundException.class, () -> stocksService.deleteById(id));
        assertEquals("Estoque não encontrado", exception.getMessage());
    }
}