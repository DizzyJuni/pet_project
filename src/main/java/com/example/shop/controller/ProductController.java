package com.example.shop.controller;

import com.example.shop.domian.Product;
import com.example.shop.dto.product.ProductDTO;
import com.example.shop.dto.product.ProductUpdateDTO;
import com.example.shop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/productCount")
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        log.info("GET /shop/productCount - page: {}, size: {}, sort: {}, direction: {}",
                page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAllProducts(pageable));
    }

    @GetMapping("/product/search-page")
    public ResponseEntity<Page<ProductDTO>> searchProductWithPagination(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/product/search-page");

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.searchProductWithPagination(name, minPrice, maxPrice, pageable));
    }


    @GetMapping("/productCount/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") UUID id) {
        log.info("GET /api/productCount/{}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductById(id));
    }

    @PostMapping("/productCount")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid Product product) {
        log.info("POST /api/productCount");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(product));
    }

    @PutMapping("/productCount/{id}")
    public ResponseEntity<ProductDTO> updateProductById(@PathVariable("id") UUID id,
                                                        @RequestBody @Valid ProductUpdateDTO product) {
        log.info("PUT /api/productCount/{}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProductById(id, product));
    }

    @DeleteMapping("/productCount/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") UUID id) {
        log.info("DELETE /api/productCount/{}", id);
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
