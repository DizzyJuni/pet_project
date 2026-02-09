package com.example.shop.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductDTO(@NotBlank String name,
                         String description,
                         @NotNull BigDecimal price,
                         @NotNull Integer stockQuantity,
                         String category
) {
}
