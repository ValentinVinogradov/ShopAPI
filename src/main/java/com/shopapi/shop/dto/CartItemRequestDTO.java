package com.shopapi.shop.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO for {@link com.shopapi.shop.models.CartItem}
 */
@Getter
@Setter
@ToString
public class CartItemRequestDTO {
    Long cartId;
    Long productId;
    Long userId;
}