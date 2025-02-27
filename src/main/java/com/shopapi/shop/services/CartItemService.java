package com.shopapi.shop.services;

import com.shopapi.shop.dto.CartItemRequestDTO;
import com.shopapi.shop.dto.CartItemResponseDTO;
import com.shopapi.shop.models.CartItem;

import java.util.UUID;


public interface CartItemService {
//    CartItemResponseDTO getCartItemById(long cartItemId);
//    void addCartItem(UUID userId, UUID productId);
//    void deleteCartItem(UUID userId, UUID productId);
    void updateCartItem(CartItem cartItem);
    void updateQuantity(CartItem cartItem, int quantityChange);
//    void deleteAllItemsByCartId(Long cartId);
}
