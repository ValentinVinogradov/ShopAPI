package com.shopapi.shop.controllers;

import com.shopapi.shop.impl.OrderItemServiceImpl;
import com.shopapi.shop.models.OrderItem;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/shop_api/v1/order_items")
public class OrderItemController {

    private final OrderItemServiceImpl orderItemService;

    public OrderItemController(OrderItemServiceImpl orderItemService) {
        this.orderItemService = orderItemService;
    }

    // Удалить все товары из заказа по ID
    @DeleteMapping("/order/{orderId}")
    public void deleteAllOrderItemsByOrderId(@PathVariable long orderId) {
        orderItemService.deleteAllItemsByContainerId(orderId);
    }

    // Получить все элементы заказа по ID заказа
    @GetMapping("/order/{orderId}")
    public List<OrderItem> getOrderItemsByOrderId(@PathVariable long orderId) {
        return orderItemService.getItemsByContainerId(orderId);
    }

    // Обновить количество конкретного товара в заказе
    @PutMapping("/update_quantity")
    public void updateQuantity(@RequestParam long orderItemId, @RequestParam int quantity) {
        orderItemService.updateQuantity(orderItemId, quantity);
    }

    // Проверить, есть ли товар в заказе
    @GetMapping("/order/{orderId}/product/{productId}/exists")
    public boolean isProductInOrder(@PathVariable long orderId, @PathVariable long productId) {
        return orderItemService.isProductInContainer(orderId, productId);
    }
}

