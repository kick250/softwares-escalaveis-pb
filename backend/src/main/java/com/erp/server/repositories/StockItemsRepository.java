package com.erp.server.repositories;

import com.erp.server.entities.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockItemsRepository extends JpaRepository<StockItem, Long> {

    public List<StockItem> findAllByDeletedFalse();

    public Optional<StockItem> findByIdAndDeletedFalse(Long id);
}
