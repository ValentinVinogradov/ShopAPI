package com.shopapi.shop.services;

import com.shopapi.shop.dto.CartItemResponseDTO;
import com.shopapi.shop.enums.CartTotalPriceOperation;
import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.models.Cart;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    List<CartItemResponseDTO> getCartItemsByUserId(long userId);
    Cart createCart(Long userId);
    PromoCodeValidationStatus applyPromoCode(long cartId, String promoCode);
    void updateTotalPrice(Cart cart, BigDecimal newTotalPrice, CartTotalPriceOperation operation);  // Обновить общую стоимость корзины
}

