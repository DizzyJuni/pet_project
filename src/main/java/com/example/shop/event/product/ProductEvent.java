package com.example.shop.event.product;

import com.example.shop.domian.Product;
import com.example.shop.enums.ProductEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ProductEvent {

    private UUID eventId;
    private ProductEventType productEventType;
    private UUID productId;
    private String productName;
    private BigDecimal price;
    private UUID categoryId;
    private Date timestamp;
    private String source;

    public static ProductEvent created(Product product) {
        return ProductEvent.builder()
                .eventId(UUID.randomUUID())
                .productEventType(ProductEventType.PRODUCT_CREATED)
                .productId(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .categoryId(product.getCategory() != null
                        ? product.getCategory().getId()
                        : null)
                .timestamp(new Date())
                .source("/api")
                .build();
    }

    public static ProductEvent updated(Product product) {
        return ProductEvent.builder()
                .eventId(UUID.randomUUID())
                .productEventType(ProductEventType.PRODUCT_UPDATED)
                .productId(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .timestamp(new Date())
                .source("/api")
                .build();
    }

    public static ProductEvent deleted(Product product) {
        return ProductEvent.builder()
                .eventId(UUID.randomUUID())
                .productEventType(ProductEventType.PRODUCT_DELETED)
                .productId(product.getId())
                .productName(product.getName())
                .timestamp(new Date())
                .source("/api")
                .build();
    }

    public static ProductEvent viewed(Product product) {
        return ProductEvent.builder()
                .eventId(UUID.randomUUID())
                .productEventType(ProductEventType.PRODUCT_VIEWED)
                .productId(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .categoryId(product.getCategory() != null
                        ? product.getCategory().getId()
                        : null)
                .timestamp(new Date())
                .source("/api")
                .build();
    }
}
