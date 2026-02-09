package com.example.shop.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(@NotBlank String name,
                          @NotBlank String slug,
                          Long productCount) {
}
