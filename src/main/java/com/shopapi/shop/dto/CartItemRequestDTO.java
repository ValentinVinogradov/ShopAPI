package com.shopapi.shop.dto;

import lombok.Value;

/**
 * DTO for {@link com.shopapi.shop.models.CartItem}
 */
@Value
public class CartItemRequestDTO {
    Long userId;
    Long cartId;
    Long productId;
}