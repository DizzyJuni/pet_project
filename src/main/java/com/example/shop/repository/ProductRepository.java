package com.example.shop.repository;

import com.example.shop.domian.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
//
//    @Query("""
//            SELECT p FROM Product
//            WHERE p.id = :id
//            """)
//    Optional<Product> findByUUID(@Param("id") UUID id);
//
//    @Modifying
//    @Query("""
//            DELETE p FROM Product
//            WHERE p.id = :id
//            """)
//    void deleteById(@Param("id") UUID id);
//
}
