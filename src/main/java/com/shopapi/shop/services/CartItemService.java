package com.shopapi.shop.services;

import com.shopapi.shop.dto.CartItemRequestDTO;
import com.shopapi.shop.models.CartItem;

import java.util.List;


public interface CartItemService {
    CartItem getCartItemById(long id);
    void addCartItem(CartItemRequestDTO cartItemRequestDTO);
    void deleteCartItem(CartItemRequestDTO cartItemRequestDTO);
    void updateCartItem(CartItem cartItem);
    List<CartItem> getItemsByCartId(Long cartId);
    void deleteAllItemsByCartId(Long cartId);
    void updateQuantity(CartItem cartItem, int quantityChange);
}
