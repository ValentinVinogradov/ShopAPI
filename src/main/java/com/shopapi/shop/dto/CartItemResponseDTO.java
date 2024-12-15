package com.shopapi.shop.dto;

import lombok.Value;

/**
 * DTO for {@link com.shopapi.shop.models.CartItem}
 */
@Value
public class CartItemResponseDTO {
    Long id;
    Long cartId;
    Long productId;
    Integer quantity;
}