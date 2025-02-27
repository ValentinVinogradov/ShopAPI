package com.shopapi.shop.repositories;

import com.shopapi.shop.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
//    void deleteAllByCart_Id(long cartId);
//    Optional<CartItem> findByCart_IdAndProduct_Id(UUID cartId, UUID productId);

    Optional<CartItem> findByIdAndCart_Id(Long cartItemId, UUID cartId);

    List<CartItem> findAllByCart_IdAndIdIn(UUID cartId, List<Long> cartItemIds);

    @Modifying
    void deleteByIdAndCart_Id(Long cartItemId, UUID cartId);

    @Modifying
//    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.product.id IN :productIds")
    void deleteAllByCart_IdAndIdIn(UUID cartId, List<Long> cartItemIds);
//    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
//    CartItem findByCartIdAndProductId(Long cartId, UUID productId);
}
