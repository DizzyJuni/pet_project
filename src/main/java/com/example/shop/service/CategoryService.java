package com.example.shop.service;

import com.example.shop.domian.Category;
import com.example.shop.dto.category.CategoryDTO;
import com.example.shop.dto.category.CategoryRequestDTO;
import com.example.shop.dto.category.CategoryUpdateDTO;
import com.example.shop.dto.mapper.MapperCategory;
import com.example.shop.exception.CategoryNotFoundException;
import com.example.shop.exception.ProductNotFoundException;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final MapperCategory mapperCategory;

    @Transactional(readOnly = true)
    @Cacheable(value = "categories",
            key = "'page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize")
    public Page<CategoryDTO> getAllCategory(Pageable pageable) {
        log.debug("Getting all categories, page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(mapperCategory::toResponse);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "category", key = "#id")
    public CategoryDTO getCategoryById(UUID id) {
        log.debug("Getting category by id: {}", id);
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        return mapperCategory.toResponse(category);
    }

    @Transactional
    @Caching(
            put = @CachePut(value = "category", key = "#result.id"),
            evict = @CacheEvict(value = "categories", allEntries = true)
    )
    public CategoryDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        log.info("Creating category: {}", categoryRequestDTO.name());

        var savedCategory = mapperCategory.toEntity(categoryRequestDTO);
        categoryRepository.save(savedCategory);

        return mapperCategory.toResponse(savedCategory);
    }

    @Transactional
    @Caching(
            put = @CachePut(value = "category", key = "#result.id"),
            evict = @CacheEvict(value = "categories", allEntries = true)
    )
    public CategoryDTO updateCategoryById(UUID id, CategoryUpdateDTO categoryUpdateDTO) {
        log.info("Updating category with id: {}", id);

        var updateCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        mapperCategory.updateEntity(updateCategory, categoryUpdateDTO);
        categoryRepository.save(updateCategory);

        return mapperCategory.toResponse(updateCategory);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "categories", allEntries = true),
                    @CacheEvict(value = "category", key = "#id")
            }
    )
    public void deleteCategoryById(UUID id) {
        log.info("Deleting category with id: {}", id);

        var deleteCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        categoryRepository.delete(deleteCategory);
    }

    public void putProductByIdInCategoryById(UUID categoryId, UUID productId) {
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        product.setCategory(category);
        productRepository.save(product);
    }
}
