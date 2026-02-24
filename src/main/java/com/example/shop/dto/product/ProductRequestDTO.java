package com.example.shop.dto.product;

import com.example.shop.domian.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDTO(@NotBlank String name,
                                String description,
                                @NotNull BigDecimal price,
                                @NotNull Integer stockQuantity,
                                Category category
                                ) {
}
