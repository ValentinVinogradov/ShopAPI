package com.shopapi.shop.dto;

import lombok.*;

/**
 * DTO for {@link com.shopapi.shop.models.CartItem}
 */
@Value
public class CartItemRequestDTO {
    Long cartId;
    Long productId;
    Long userId;
}