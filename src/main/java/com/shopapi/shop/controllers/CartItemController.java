package com.shopapi.shop.controllers;

import com.shopapi.shop.impl.CartItemServiceImpl;
import com.shopapi.shop.models.CartItem;
import com.shopapi.shop.models.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shop_api/v1/cart_items")
public class CartItemController{
    private final CartItemServiceImpl cartItemService;

    public CartItemController(CartItemServiceImpl cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/add/product/{productId}")
    public ResponseEntity<String> addNewCartItem(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID productId) {
        try {
            cartItemService.addNewItem(principal.getId(), productId);
            URI uri = new URI("/shop_api/v1/cart_items");
            return ResponseEntity.created(uri)
                    .body("Cart item added successfully!"); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to add cart item: " + e.getMessage()); // 400 Bad Request
        }
    }

    @PostMapping("/increase/{cartItemId}")
    public ResponseEntity<String> increaseQuantity(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long cartItemId) {
        try {
            cartItemService.increaseQuantity(principal.getId(), cartItemId);
            return ResponseEntity.ok("Successfully increased quantity!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to increase quantity");
        }
    }

    @PostMapping("/decrease/{cartItemId}")
    public ResponseEntity<String> decreaseQuantity(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long cartItemId) {
        try {
            cartItemService.decreaseQuantity(principal.getId(), cartItemId);
            return ResponseEntity.ok("Successfully decreased quantity!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to decrease quantity");
        }
    }


    //todo респонс дто добавить не весь же CartItem пихать
    //todo нахуй вообще апдейт нужен
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

    @DeleteMapping("/delete/selected-items")
    public ResponseEntity<String> deleteSelectedItems(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody List<Long> cartItemIds) {
        try {
            cartItemService.deleteSelectedCartItems(principal.getId(), cartItemIds);
            return ResponseEntity.ok("Selected items successfully deleted!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete selected items");
        }
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long cartItemId) {
        try {
            cartItemService.deleteCartItemById(principal.getId(), cartItemId);
            return ResponseEntity.ok("Cart item deleted successfully!"); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Failed to delete cart item: " + e.getMessage()); // 400 Bad Request
        }
    }

    //todo подумать потом над селектом (реализовал удаление) (надо ли?)
//    @DeleteMapping("/delete/cart/{cartId}/all")
//    public ResponseEntity<String> deleteAllCartItems(@PathVariable long cartId) {
//        try {
//            cartItemService.deleteAllItemsByCartId(cartId);
//            return ResponseEntity.ok("All items from cart deleted successfully!"); // 200 OK
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                    .body("Failed to delete items: " + e.getMessage()); // 400 Bad Request
//        }
//    }

}
