package com.erp.server.repositories;

import com.erp.server.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Product, Long> {

    public List<Product> findAllByDeletedFalse();

    public Optional<Product> findByIdAndDeletedFalse(Long id);
}
