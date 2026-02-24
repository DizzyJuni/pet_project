package com.example.shop.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDTO(@NotNull UUID id,
                      String email,
                      @NotBlank String userName,
                      @NotBlank String firstName,
                      @NotBlank String lastName,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt) {
}
