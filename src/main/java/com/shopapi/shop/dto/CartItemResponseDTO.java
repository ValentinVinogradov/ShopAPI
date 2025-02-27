package com.shopapi.shop.dto;


import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for {@link com.shopapi.shop.models.CartItem}
 */
public record CartItemResponseDTO(
        Long id,
        UUID cartId,
        UUID productId,
        String productName,
        String productDescription,
        BigDecimal productPrice,
        String productFirstImg,
        Integer quantity
) {}