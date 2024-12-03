package com.shopapi.shop.controller;


import com.shopapi.shop.dto.CartItemRequestDTO;
import com.shopapi.shop.impl.CartItemServiceImpl;
import com.shopapi.shop.models.CartItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/cart_items")
public class CartItemController {
    private final CartItemServiceImpl cartItemService;

    public CartItemController(CartItemServiceImpl cartItemService) {
//        super(cartItemService);
        this.cartItemService = cartItemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getCartItemById(@PathVariable long id) {
        CartItem cartItem = cartItemService.getCartItemById(id);
        if (cartItem != null) {
            return ResponseEntity.ok(cartItem); // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
        }
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItem>> getCartItemsByCartId(@PathVariable long cartId) {
        List<CartItem> cartItems = cartItemService.getItemsByCartId(cartId);
        if (!cartItems.isEmpty()) {
            return ResponseEntity.ok(cartItems); // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList()); // 404 Not Found
        }
    }

    @PostMapping("/add_item")
    public ResponseEntity<String> addCartItem(@RequestBody CartItemRequestDTO cartItemRequestDTO) {
        try {
            cartItemService.addCartItem(cartItemRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Cart item added successfully."); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to add cart item: " + e.getMessage()); // 400 Bad Request
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCartItem(@RequestBody CartItem cartItem) {
        try {
            cartItemService.updateCartItem(cartItem);
            return ResponseEntity.ok("Cart item updated successfully."); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update cart item: " + e.getMessage()); // 400 Bad Request
        }
    }

    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<String> deleteAllCartItemsByCartId(@PathVariable long cartId) {
        try {
            cartItemService.deleteAllItemsByCartId(cartId);
            return ResponseEntity.ok("All items from cart deleted successfully."); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to delete items: " + e.getMessage()); // 400 Bad Request
        }
    }

    @DeleteMapping("/delete_item")
    public ResponseEntity<String> deleteCartItem(@RequestBody CartItemRequestDTO cartItemRequestDTO) {
        try {
            cartItemService.deleteCartItem(cartItemRequestDTO);
            return ResponseEntity.ok("Cart item deleted successfully."); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to delete cart item: " + e.getMessage()); // 400 Bad Request
        }
    }
}
