package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.OrderRequestDTO;
import com.shopapi.shop.dto.PreviewRequestDTO;
import com.shopapi.shop.dto.OrderResponseDTO;
import com.shopapi.shop.dto.PreviewResponseDTO;
import com.shopapi.shop.impl.OrderServiceImpl;
import com.shopapi.shop.models.Order;
import com.shopapi.shop.models.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/shop_api/v1/orders")
public class OrderController {
    private final OrderServiceImpl orderService;


    //todo сделать как то проверку на наличие товара как на вб

    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    // получение по id
    @GetMapping("/user/all")
    public ResponseEntity<List<OrderResponseDTO>> getAllUserOrders(@AuthenticationPrincipal UserPrincipal principal) {
        try {
            return ResponseEntity.ok(orderService.getOrdersByUserId(principal.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/user/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable UUID orderId) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(orderId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@AuthenticationPrincipal UserPrincipal principal,
                                              @RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            orderService.createOrder(principal.getId(), orderRequestDTO);
            return ResponseEntity.ok("Order created successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create order");
        }
    }

    @PostMapping("/order-preview")
    public ResponseEntity<PreviewResponseDTO> showOrderPreview(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody PreviewRequestDTO previewRequestDTO
    ) {
        try {
            return ResponseEntity.ok(orderService.showOrderPreview(principal.getId(), previewRequestDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/change-status/{orderId}")
    public ResponseEntity<String> changeOrderStatus(@PathVariable UUID orderId) {
        return null;
    }

    @PostMapping("/user/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable UUID orderId) {
        return null;
    }

    @GetMapping("/status/{status}")
    public List<Order> getOrdersByStatus(@PathVariable String status) {
        return orderService.getOrdersByStatus(status);
    }
}
