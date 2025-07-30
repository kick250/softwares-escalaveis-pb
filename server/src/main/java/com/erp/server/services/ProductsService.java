package com.erp.server.services;

import com.erp.server.entities.Product;
import com.erp.server.exceptions.ProductNotFoundException;
import com.erp.server.repositories.ProductsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {
    private final ProductsRepository productsRepository;

    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<Product> getAll() {
        return productsRepository.findAllByDeletedFalse();
    }

    public Product getById(Long id) throws ProductNotFoundException {
        return productsRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public void create(String name, String description) {
        Product product = new Product(name, description);
        productsRepository.save(product);
    }

    public void update(Long id, String name, String description) throws ProductNotFoundException {
        Product product = getById(id);
        product.setName(name);
        product.setDescription(description);
        productsRepository.save(product);
    }

    public void deleteById(Long id) throws ProductNotFoundException {
        Product product = getById(id);
        product.delete();
        productsRepository.save(product);
    }
}
