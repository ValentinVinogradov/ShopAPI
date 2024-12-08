package com.shopapi.shop.services;

import com.shopapi.shop.enums.CartTotalPriceOperation;
import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.models.Cart;

import java.math.BigDecimal;

public interface CartService extends GenericService<Cart, Long> {
    void createCart(Long userId);
    PromoCodeValidationStatus applyPromoCode(long cartId, String promoCode);
    void updateTotalPrice(Cart cart, BigDecimal newTotalPrice, CartTotalPriceOperation operation);  // Обновить общую стоимость корзины
}

