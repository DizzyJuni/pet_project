package com.example.shop.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateDTO(@NotBlank String name,
                                @NotBlank String slug) {
}
