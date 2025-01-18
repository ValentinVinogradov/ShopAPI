package com.shopapi.shop.repositories;

import com.shopapi.shop.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCart_Id(long cartId);
//    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
//    CartItem findByCartIdAndProductId(Long cartId, Long productId);
}
