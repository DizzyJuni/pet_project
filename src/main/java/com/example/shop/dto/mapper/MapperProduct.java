package com.example.shop.dto.mapper;

import com.example.shop.domian.Product;
import com.example.shop.dto.product.ProductDTO;
import com.example.shop.dto.product.ProductRequestDTO;
import com.example.shop.dto.product.ProductUpdateDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MapperProduct {

    public ProductDTO toResponse(Product productRequest) {
        return new ProductDTO(productRequest.getId(),
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice(),
                productRequest.getStockQuantity(),
                productRequest.getCategory() != null
                        ? productRequest.getCategory().getName()
                        : null,
                productRequest.getCategory() != null
                        ? productRequest.getCategory().getSlug()
                        : null
        );
    }

    public Product toEntity(ProductRequestDTO productRequestDTO) {
        return Product.builder()
                .name(productRequestDTO.name())
                .description(productRequestDTO.description())
                .price(productRequestDTO.price())
                .stockQuantity(productRequestDTO.stockQuantity())
                .category(productRequestDTO.category())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void updateEntity(Product product, ProductUpdateDTO productDto) {
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        product.setStockQuantity(productDto.stockQuantity());
        product.setUpdatedAt(LocalDateTime.now());
    }
}
