package com.shopapi.shop.dto;

import java.util.UUID;

/**
 * DTO for {@link com.shopapi.shop.models.CartItem}
 */
public record CartItemRequestDTO(
//        Long id,
//        Long cartId,
        UUID productId
) {}