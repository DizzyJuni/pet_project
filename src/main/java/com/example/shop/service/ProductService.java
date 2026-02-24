package com.example.shop.service;

import com.example.shop.domian.Product;
import com.example.shop.dto.product.ProductDTO;
import com.example.shop.dto.product.ProductRequestDTO;
import com.example.shop.dto.product.ProductUpdateDTO;
import com.example.shop.dto.mapper.MapperProduct;
import com.example.shop.event.product.ProductEvent;
import com.example.shop.event.product.ProductEventProducer;
import com.example.shop.exception.ProductNotFoundException;
import com.example.shop.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductEventProducer productEventProducer;
    private final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final MapperProduct mapperProduct;

    @Transactional(readOnly = true)
    @Cacheable(value = "products",
            key = "'page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize")
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        log.debug("Getting all products, page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        Page<Product> productList = productRepository.findAll(pageable);
        return productList.map(mapperProduct::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> searchProductWithPagination(String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.info("Searching products - name: {}, price: {}-{}, page: {}, size: {}",
                name, minPrice, maxPrice,
                pageable.getPageNumber(), pageable.getPageSize());

        String searchName = StringUtils.hasText(name) ? name.trim() : null;

        Page<Product> searchProductList = productRepository.findAll(
                createSearchSpecification(searchName, minPrice, maxPrice), pageable
        );

        return searchProductList.map(mapperProduct::toResponse);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "product", key = "#id")
    public ProductDTO getProductById(UUID id) {
        log.debug("Getting product by id: {}", id);
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        ProductEvent event = ProductEvent.viewed(product);
        productEventProducer.sendProductEvent(event);

        return mapperProduct.toResponse(product);
    }

    @Transactional
    @Caching(
            put = @CachePut(value = "product", key = "#result.id"),
            evict = @CacheEvict(value = "products", allEntries = true)
    )
    public ProductDTO createProduct(ProductRequestDTO productRequestDTO) {
        log.info("Creating product: {}", productRequestDTO.name());
        var productToSave = mapperProduct.toEntity(productRequestDTO);
        productRepository.save(productToSave);

        ProductEvent event = ProductEvent.created(productToSave);
        productEventProducer.sendProductEvent(event);

        return mapperProduct.toResponse(productToSave);
    }

    @Transactional
    @Caching(
            put = @CachePut(value = "product", key = "#result.id"),
            evict = @CacheEvict(value = "products", allEntries = true)
    )
    public ProductDTO updateProductById(UUID id, ProductUpdateDTO productUpdateDTO) {
        log.info("Updating product with id: {}", id);
        var updateProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        mapperProduct.updateEntity(updateProduct, productUpdateDTO);
        productRepository.save(updateProduct);

        ProductEvent event = ProductEvent.updated(updateProduct);
        productEventProducer.sendProductEvent(event);

        return mapperProduct.toResponse(updateProduct);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "product", key = "#id"),
                    @CacheEvict(value = "products", allEntries = true)
            }
    )
    public void deleteProductById(UUID id) {
        log.info("Deleting product with id: {}", id);

        var deleteProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        productRepository.delete(deleteProduct);

        ProductEvent event = ProductEvent.deleted(deleteProduct);
        productEventProducer.sendProductEvent(event);
    }

    private Specification<Product> createSearchSpecification(
            String name, BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                ));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"), minPrice
                ));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"), maxPrice
                ));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
