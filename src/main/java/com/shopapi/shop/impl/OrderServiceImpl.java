package com.shopapi.shop.impl;

import com.shopapi.shop.models.Order;
import com.shopapi.shop.repositories.OrderRepository;
import com.shopapi.shop.services.AbstractService;
import com.shopapi.shop.services.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl extends AbstractService<Order, Long> implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getOrdersByUserId(long userId) {
        return List.of();
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return List.of();
    }


    @Override
    public BigDecimal calculateTotalPrice(long orderId) {
        return null;
    }
}
