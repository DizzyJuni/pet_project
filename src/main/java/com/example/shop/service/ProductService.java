package com.example.shop.service;

import com.example.shop.domian.Product;
import com.example.shop.dto.ProductDTO;
import com.example.shop.dto.ProductUpdateDTO;
import com.example.shop.dto.mapper.MapperProduct;
import com.example.shop.exception.ProductNotFoundException;
import com.example.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final MapperProduct mapperProduct;

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        log.info("Start method getAllProduct.");
        List<Product> productList = productRepository.findAll();
        return productList.stream()
                .map(mapperProduct::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(UUID id) {
        log.info("Start method getProductById: {}", id);
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return mapperProduct.toResponse(product);
    }

    @Transactional
    public ProductDTO createProduct(Product product) {
        log.info("Start method createProduct");
        var productToSave = productRepository.save(product);
        return mapperProduct.toResponse(productToSave);
    }

    @Transactional
    public ProductDTO updateProductById(UUID id, ProductUpdateDTO productUpdateDTO) {
        log.info("Start method updateProductById: {}", id);
        var updateProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        updateProduct.setName(productUpdateDTO.name());
        updateProduct.setDescription(productUpdateDTO.description());
        updateProduct.setPrice(productUpdateDTO.price());
        updateProduct.setStockQuantity(productUpdateDTO.stockQuantity());

        return mapperProduct.toResponse(updateProduct);
    }

    @Transactional
    public void deleteProductById(UUID id) {
        log.info("Start method deleteProductById: {}", id);

        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
