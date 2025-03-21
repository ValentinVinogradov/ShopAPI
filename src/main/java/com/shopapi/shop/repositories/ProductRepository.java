package com.shopapi.shop.repositories;

import com.shopapi.shop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
//    @Query("SELECT p FROM Product p WHERE p.name = ?1")
    Optional<Product> findProductByName(String name);
}

