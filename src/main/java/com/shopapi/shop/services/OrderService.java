package com.shopapi.shop.services;

import com.shopapi.shop.models.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderService extends GenericService<Order, Long> {
    List<Order> getOrdersByUserId(UUID userId);     // Получить все заказы пользователя по его ID
    List<Order> getOrdersByStatus(String status);   // Получить заказы по статусу
    BigDecimal calculateTotalPrice(UUID orderId);   // Рассчитать общую стоимость заказа
}