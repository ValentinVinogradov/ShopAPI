package com.shopapi.shop.dto;

import com.shopapi.shop.enums.OrderPaymentStatus;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.shopapi.shop.models.Order}
 */

public record OrderRequestDTO (
    List<Long> cartItemIds,
    OrderPaymentStatus orderPaymentStatus,
    BigDecimal shippingCost
) {}