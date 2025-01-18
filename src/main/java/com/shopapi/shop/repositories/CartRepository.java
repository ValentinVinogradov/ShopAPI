package com.shopapi.shop.repositories;

import com.shopapi.shop.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
//    @Query("SELECT c FROM Cart c WHERE c.user.id = ?1")
    Optional<Cart> findByUser_Id(Long userId);
}
