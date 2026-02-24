package com.example.shop.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(@NotBlank String name,
                                 @NotBlank String slug) {
}
