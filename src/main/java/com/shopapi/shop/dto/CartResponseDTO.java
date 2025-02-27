package com.shopapi.shop.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponseDTO(
    String shippingAddress,
    List<CartItemResponseDTO> cartItems,
    BigDecimal totalPrice,
    Integer totalCount
) {}
