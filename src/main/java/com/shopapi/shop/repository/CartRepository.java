package com.shopapi.shop.repository;

import com.shopapi.shop.models.Cart;
import com.shopapi.shop.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.id = ?1")
    Cart findByUserId(Long userId);
}
