package com.shopapi.shop.services;

import com.shopapi.shop.dto.CartResponseDTO;
import com.shopapi.shop.enums.CartTotalPriceOperation;
import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.models.Cart;

import java.math.BigDecimal;
import java.util.UUID;

public interface CartService {
    CartResponseDTO getCartById(UUID userId);
    Cart createCart(UUID userId);
    PromoCodeValidationStatus applyPromoCode(UUID cartId, String promoCode);
    void updateTotalPrice(Cart cart, BigDecimal newTotalPrice, CartTotalPriceOperation operation);  // Обновить общую стоимость корзины
}

