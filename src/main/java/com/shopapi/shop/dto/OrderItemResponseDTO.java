package com.shopapi.shop.dto;


import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for {@link com.shopapi.shop.models.OrderItem}
 */
public record OrderItemResponseDTO(
        UUID productId,
        Integer quantity,
        BigDecimal productPrice
) {}