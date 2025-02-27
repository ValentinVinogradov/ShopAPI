package com.shopapi.shop.dto;

import com.shopapi.shop.enums.OrderPaymentStatus;

import java.util.List;

/**
 * DTO for {@link com.shopapi.shop.models.Order}
 */
public record PreviewRequestDTO(
        List<Long> cartItemIds,
        OrderPaymentStatus paymentStatus //todo enum или стринг
) {}
