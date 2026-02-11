package com.example.shop.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(@NotNull UUID id,
                         @NotBlank String name,
                         String description,
                         @NotNull BigDecimal price,
                         @NotNull Integer stockQuantity,
                         String categoryName,
                         String categorySlug
) {
}
