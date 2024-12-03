package com.shopapi.shop.controller;

import com.shopapi.shop.impl.OrderServiceImpl;
import com.shopapi.shop.models.Order;
import com.shopapi.shop.services.GenericService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/shop_api/v1/orders")
public class OrderController extends GenericController<Order, Long>{
    private final OrderServiceImpl orderService;

    public OrderController(GenericService<Order, Long> service, OrderServiceImpl orderService) {
        super(service);
        this.orderService = orderService;
    }
    

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @GetMapping("/status/{status}")
    public List<Order> getOrdersByStatus(@PathVariable String status) {
        return orderService.getOrdersByStatus(status);
    }

    @GetMapping("/calculate-total/{orderId}")
    public BigDecimal calculateTotalPrice(@PathVariable long orderId) {
        return orderService.calculateTotalPrice(orderId);
    }






}
