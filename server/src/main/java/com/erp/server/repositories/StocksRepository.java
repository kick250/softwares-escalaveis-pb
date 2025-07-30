package com.erp.server.repositories;

import com.erp.server.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StocksRepository extends JpaRepository<Stock, Long> {

    public List<Stock> findAllByDeletedFalse();

    public Optional<Stock> findByIdAndDeletedFalse(Long id);
}
