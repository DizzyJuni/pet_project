package com.example.shop.dto.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CartDTO(UUID productId,
                      String productName,
                      BigDecimal price,
                      Integer quantity,
                      LocalDateTime addedAt) {
}
