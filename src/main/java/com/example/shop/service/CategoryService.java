package com.example.shop.service;

import com.example.shop.domian.Category;
import com.example.shop.dto.category.CategoryDTO;
import com.example.shop.dto.category.CategoryUpdateDTO;
import com.example.shop.dto.mapper.MapperCategory;
import com.example.shop.exception.CategoryNotFoundException;
import com.example.shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final MapperCategory mapperCategory;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> getAllCategory(Pageable pageable) {
        log.info("Start method getAllCategory with pageable");
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(mapperCategory::toResponse);
    }

    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(UUID id) {
        log.info("Start method getCategoryById:{}", id);
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        return mapperCategory.toResponse(category);
    }

    @Transactional
    public CategoryDTO createCategory(Category category) {
        log.info("Start method createCategory.");
        var savedCategory = categoryRepository.save(category);
        return mapperCategory.toResponse(savedCategory);
    }

    @Transactional
    public CategoryDTO updateCategoryById(UUID id, CategoryUpdateDTO categoryUpdateDTO) {
        log.info("Start method updateCategory.");
        var updateCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        updateCategory.setName(categoryUpdateDTO.name());
        updateCategory.setSlug(categoryUpdateDTO.slug());
        return mapperCategory.toResponse(updateCategory);
    }

    @Transactional
    public void deleteCategoryById(UUID id) {
        log.info("Start method deleteCategoryById");
        var deleteCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        categoryRepository.delete(deleteCategory);
    }
}
