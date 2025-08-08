package com.erp.server.controllers;

import com.erp.server.entities.Product;
import com.erp.server.exceptions.ProductNotFoundException;
import com.erp.server.requests.ProductCreateRequest;
import com.erp.server.requests.ProductUpdateRequest;
import com.erp.server.responses.ProductResponse;
import com.erp.server.responses.ProductsResponse;
import com.erp.server.services.ProductsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductsController {
    private final ProductsService productsService;

    @GetMapping
    @Transactional
    public ResponseEntity<ProductsResponse> getProducts() {
        List<Product> products = productsService.getAll();
        return ResponseEntity.ok(new ProductsResponse(products));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        try {
            Product product = productsService.getById(id);
            return ResponseEntity.ok(new ProductResponse(product));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = "multipart/form-data")
    @Transactional
    public ResponseEntity<String> create(@Valid @ModelAttribute ProductCreateRequest request) throws IOException {
        productsService.create(request.getName(), request.getDescription(), request.getImageBytes(), request.getImageContentType());
        return ResponseEntity.status(201).body("Produto criado com sucesso");
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> update(@PathVariable Long id, @Valid @ModelAttribute ProductUpdateRequest request) throws IOException {
        try {
            productsService.update(id, request.getName(), request.getDescription(), request.getImageBytes(), request.getImageContentType());
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

    @GetMapping("/{id}/image")
    @Transactional
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        try {
            byte[] imageBytes = productsService.getProductImage(id);
            String imageType = productsService.getImageType(id);

            return ResponseEntity
                    .ok()
                    .header("Content-Type", imageType)
                    .body(imageBytes);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
