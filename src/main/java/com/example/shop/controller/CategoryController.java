package com.example.shop.controller;

import com.example.shop.dto.category.CategoryDTO;
import com.example.shop.dto.category.CategoryRequestDTO;
import com.example.shop.dto.category.CategoryUpdateDTO;
import com.example.shop.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final Logger log = LoggerFactory.getLogger(CategoryController.class);

    @GetMapping("/categories")
    public ResponseEntity<Page<CategoryDTO>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /shop/categories");
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.getAllCategory(pageable));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("id") @Valid UUID id) {
        log.info("GET /shop/categoty/{}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.getCategoryById(id));
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CategoryRequestDTO category) {
        log.info("POST /shop/category");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.createCategory(category));
    }

    @PutMapping("category/{id}")
    public ResponseEntity<CategoryDTO> updateCategoryById(@PathVariable("id") UUID id,
                                                          @RequestBody @Valid CategoryUpdateDTO categoryUpdateDTO) {
        log.info("PUT /shop/category/{}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.updateCategoryById(id, categoryUpdateDTO));
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("id") UUID id) {
        log.info("DELETE /shop/category/{}", id);
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/category/addProductById/categoryId/{categoryId}/productId/{productId}")
    public ResponseEntity<String> addProductToCategoryById(
            @PathVariable("categoryId") UUID categoryId,
            @PathVariable("productId") UUID productId) {
        log.info("PUT /shop/category/addProductById/categoryId={}&productId={}",
                categoryId, productId);
        categoryService.putProductByIdInCategoryById(categoryId, productId);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
