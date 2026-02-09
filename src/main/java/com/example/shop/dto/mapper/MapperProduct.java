package com.example.shop.dto.mapper;

import com.example.shop.domian.Product;
import com.example.shop.dto.product.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class MapperProduct {

    public ProductDTO toResponse(Product productRequest) {
        return new ProductDTO(productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice(),
                productRequest.getStockQuantity(),
                productRequest.getCategory() != null
                        ? productRequest.getCategory().getName()
                        : null
        );
    }

    private Product toEntity(ProductDTO productResponse) {
        return Product.builder()
                .name(productResponse.name())
                .description(productResponse.description())
                .price(productResponse.price())
                .stockQuantity(productResponse.stockQuantity())
                .build();
    }

    public void updateEntity(Product product, ProductDTO productDto) {
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        product.setStockQuantity(productDto.stockQuantity());
    }
}
