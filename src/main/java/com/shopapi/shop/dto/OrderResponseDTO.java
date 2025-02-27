package com.shopapi.shop.dto;

import com.shopapi.shop.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link com.shopapi.shop.models.Order}
 */
public record OrderResponseDTO(
        List<OrderItemResponseDTO> orderItems,
        OrderStatus orderStatus,
        BigDecimal totalPrice,
        Integer totalCount,
        String shippingAddress,
        BigDecimal shippingCost,
        LocalDate deliveryDate,
        String trackingNumber
) {}