package com.shopapi.shop.controllers;


import com.shopapi.shop.dto.CartItemRequestDTO;
import com.shopapi.shop.dto.CartItemResponseDTO;
import com.shopapi.shop.impl.CartItemServiceImpl;
import com.shopapi.shop.models.CartItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/cart_items")
public class CartItemController{
    private final CartItemServiceImpl cartItemService;

    public CartItemController(CartItemServiceImpl cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItemResponseDTO> getCartItemById(@PathVariable long id) {
        try {
            CartItemResponseDTO cartItemResponseDTO = cartItemService.getCartItemById(id);
            return ResponseEntity.ok(cartItemResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCartItem(@RequestBody CartItemRequestDTO cartItemRequestDTO) {
        try {
            cartItemService.addCartItem(cartItemRequestDTO);
            URI uri = new URI("/shop_api/v1/cart_items");
            return ResponseEntity.created(uri)
                    .body("Cart item added successfully!"); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to add cart item: " + e.getMessage()); // 400 Bad Request
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCartItem(@RequestBody CartItem cartItem) {
        try {
            cartItemService.updateCartItem(cartItem);
            return ResponseEntity.ok("Cart item updated successfully!"); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to update cart item: " + e.getMessage()); // 400 Bad Request
        }
    }

    @DeleteMapping("/delete/cart/{cartId}/selected-items")
    public ResponseEntity<String> deleteSelectedItems(@RequestBody List<Long> cartItemIds) {
        try {
            cartItemService.deleteSelectedCartItems(cartItemIds);
            return ResponseEntity.ok("Selected items successfully deleted!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete selected items");
        }
    }

    //todo подумать потом над селектом (реализовал удаление)
    @DeleteMapping("/delete/cart/{cartId}/all")
    public ResponseEntity<String> deleteAllCartItems(@PathVariable long cartId) {
        try {
            cartItemService.deleteAllItemsByCartId(cartId);
            return ResponseEntity.ok("All items from cart deleted successfully!"); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to delete items: " + e.getMessage()); // 400 Bad Request
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCartItem(@RequestBody CartItemRequestDTO cartItemRequestDTO) {
        try {
            cartItemService.deleteCartItem(cartItemRequestDTO);
            return ResponseEntity.ok("Cart item deleted successfully!"); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to delete cart item: " + e.getMessage()); // 400 Bad Request
        }
    }
}
