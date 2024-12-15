package com.shopapi.shop.services;

import com.shopapi.shop.dto.CartItemRequestDTO;
import com.shopapi.shop.dto.CartItemResponseDTO;
import com.shopapi.shop.models.CartItem;


public interface CartItemService {
    CartItemResponseDTO getCartItemById(long cartItemId);
    void addCartItem(CartItemRequestDTO cartItemRequestDTO);
    void deleteCartItem(CartItemRequestDTO cartItemRequestDTO);
    void updateCartItem(CartItem cartItem);
    void updateQuantity(CartItem cartItem, int quantityChange);
    void deleteAllItemsByCartId(Long cartId);
}
