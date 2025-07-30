package com.erp.server.controllers;

import com.erp.server.entities.Product;
import com.erp.server.exceptions.ProductNotFoundException;
import com.erp.server.requests.ProductCreateRequest;
import com.erp.server.requests.ProductUpdateRequest;
import com.erp.server.responses.ProductResponse;
import com.erp.server.responses.ProductsResponse;
import com.erp.server.services.ProductsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public ResponseEntity<ProductsResponse> getProducts() {
        List<Product> products = productsService.getAll();
        return ResponseEntity.ok(new ProductsResponse(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        try {
            Product product = productsService.getById(id);
            return ResponseEntity.ok(new ProductResponse(product));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> create(@Valid @RequestBody ProductCreateRequest request) {
        productsService.create(request.name(), request.description());
        return ResponseEntity.status(201).body("Produto criado com sucesso");
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        try {
            productsService.update(id, request.name(), request.description());
            return ResponseEntity.ok("Produto atualizado com sucesso");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            productsService.deleteById(id);
            return ResponseEntity.ok("Produto deletado com sucesso");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
