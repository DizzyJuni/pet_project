package com.example.shop.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CategoryDTO(@NotNull UUID id,
                          @NotBlank String name,
                          @NotBlank String slug,
                          Long productCount) {
}
