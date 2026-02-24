package com.example.shop.dto.mapper;

import com.example.shop.domian.Category;
import com.example.shop.dto.category.CategoryDTO;
import com.example.shop.dto.category.CategoryRequestDTO;
import com.example.shop.dto.category.CategoryUpdateDTO;
import org.springframework.stereotype.Component;

@Component
public class MapperCategory {

    public CategoryDTO toResponse(Category categoryRequest) {
        return new CategoryDTO(categoryRequest.getId(),
                categoryRequest.getName(),
                categoryRequest.getSlug(),
                categoryRequest.getProducts() != null
                        ? (long) categoryRequest.getProducts().size()
                        : null
        );
    }

    public Category toEntity(CategoryRequestDTO categoryDTO) {
        return Category.builder()
                .name(categoryDTO.name())
                .slug(categoryDTO.slug())
                .build();
    }

    public void updateEntity (Category category, CategoryUpdateDTO categoryUpdateDTO) {
        category.setName(categoryUpdateDTO.name());
        category.setSlug(categoryUpdateDTO.slug());
    }
}
