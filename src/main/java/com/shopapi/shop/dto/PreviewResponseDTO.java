package com.shopapi.shop.dto;


import java.math.BigDecimal;
import java.util.List;

public record PreviewResponseDTO(
        String address,
//        List<String> payment,
        Integer totalCount,
        BigDecimal totalPrice,
        BigDecimal discountPrice,
        BigDecimal promocodePrice,
        BigDecimal certificatePrice,
        List<CartItemResponseDTO> cartItems
) {}
