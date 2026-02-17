package com.example.shop.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateDTO(String email,
                            @NotBlank String userName,
                            @NotBlank String firstName,
                            @NotBlank String lastName) {
}
