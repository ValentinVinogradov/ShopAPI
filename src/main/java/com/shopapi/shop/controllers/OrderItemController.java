package com.shopapi.shop.controllers;

import com.shopapi.shop.impl.OrderItemServiceImpl;
import com.shopapi.shop.models.OrderItem;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/shop_api/v1/order_items")
public class OrderItemController {

    private final OrderItemServiceImpl orderItemService;

    public OrderItemController(OrderItemServiceImpl orderItemService) {
        this.orderItemService = orderItemService;
    }

    // Удалить все товары из заказа по ID
    @DeleteMapping("/order/{orderId}")
    public void deleteAllOrderItemsByOrderId(@PathVariable UUID orderId) {
    }

    // Получить все элементы заказа по ID заказа
    @GetMapping("/order/{orderId}")
    public List<OrderItem> getOrderItemsByOrderId(@PathVariable UUID orderId) {
        return null;
    }

    // Обновить количество конкретного товара в заказе
    @PutMapping("/update_quantity")
    public void updateQuantity(@RequestParam long orderItemId, @RequestParam int quantity) {
    }

    // Проверить, есть ли товар в заказе
    @GetMapping("/order/{orderId}/product/{productId}/exists")
    public boolean isProductInOrder(@PathVariable UUID orderId, @PathVariable UUID productId) {
        return false;
    }
}

