package com.shopapi.shop.services;

import com.shopapi.shop.enums.CartTotalPriceOperation;
import com.shopapi.shop.models.Cart;
import com.shopapi.shop.models.CartItem;

import java.math.BigDecimal;
import java.util.List;

public interface CartService extends GenericService<Cart, Long> {
    void updateTotalPrice(Cart cart, BigDecimal newTotalPrice, CartTotalPriceOperation operation);  // Обновить общую стоимость корзины
}

