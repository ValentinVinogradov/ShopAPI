package com.shopapi.shop.services;

import com.shopapi.shop.models.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService extends GenericService<Order, Long> {
    List<Order> getOrdersByUserId(long userId);     // Получить все заказы пользователя по его ID
    List<Order> getOrdersByStatus(String status);   // Получить заказы по статусу
    BigDecimal calculateTotalPrice(long orderId);   // Рассчитать общую стоимость заказа
}