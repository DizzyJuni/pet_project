package com.example.shop.repository;

import com.example.shop.domian.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>,
        JpaSpecificationExecutor<Product> {

//    @Query("""
//            SELECT p FROM Product p
//            WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%',:name ,'%')))
//            AND (:minPrice IS NULL OR p.price >= :minPrice)
//            AND (:maxPrice IS NULL OR p.price <= maxPrice)
//            """)
//    Page<Product> searchProductWithPagination(@Param("name") String name,
//                                              @Param("minPrice") BigDecimal minPrice,
//                                              @Param("maxPrice") BigDecimal maxPrice);
//
//    Page<Product> findByCategoryById(UUID categoryId, Pageable pageable);
//
//    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
