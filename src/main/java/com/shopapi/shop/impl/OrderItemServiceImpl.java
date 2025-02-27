package com.shopapi.shop.impl;

import com.shopapi.shop.models.OrderItem;
import com.shopapi.shop.repositories.OrderItemRepository;
import com.shopapi.shop.services.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/*

 */
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final CartServiceImpl cartService;
    private final OrderItemRepository orderItemRepository;

    public OrderItemServiceImpl(CartServiceImpl cartService,
                                OrderItemRepository orderItemRepository) {
        this.cartService = cartService;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderItem getOrderItemById(long orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found"));
    }

    public void makeSelectedOrderItems(List<Long> cartItemIds) {
        
    }

}
