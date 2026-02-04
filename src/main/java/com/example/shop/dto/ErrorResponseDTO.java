package com.example.shop.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(String message,
                               String detailMessage,
                               LocalDateTime errorTime) {
}
