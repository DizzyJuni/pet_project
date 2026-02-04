package com.example.shop.controller;

import com.example.shop.domian.Product;
import com.example.shop.dto.ProductDTO;
import com.example.shop.dto.ProductUpdateDTO;
import com.example.shop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        log.info("GET /api/products");
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") UUID id) {
        log.info("GET /api/products/{}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductById(id));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid Product product) {
        log.info("POST /api/products");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProductById(@PathVariable("id") UUID id,
                                                        @RequestBody @Valid ProductUpdateDTO product) {
        log.info("PUT /api/products/{}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProductById(id, product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") UUID id) {
        log.info("DELETE /api/products/{}", id);
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
