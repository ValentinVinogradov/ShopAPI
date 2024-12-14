package com.shopapi.shop.controller;

import com.shopapi.shop.dto.CartItemResponseDTO;
import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.impl.CartItemServiceImpl;
import com.shopapi.shop.impl.CartServiceImpl;
import com.shopapi.shop.models.Cart;
import com.shopapi.shop.models.CartItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/carts")
public class CartController {
    private final CartServiceImpl cartService;

    private final CartItemServiceImpl cartItemService;

    public CartController(CartServiceImpl cartService,
                          CartItemServiceImpl cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartItemResponseDTO>> getCartItemsFromCartByUserId(@PathVariable long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCartItemsByUserId(userId));
    }

    @PostMapping("/create_cart/user/{userId}")
    public ResponseEntity<String> createCart(@PathVariable long userId) {
        cartService.createCart(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cart created successfully");
    }

    @PostMapping("/{cartId}/apply_promo")
    public ResponseEntity<String> applyPromoCode(@PathVariable long cartId, @RequestParam String promoCode) {
        PromoCodeValidationStatus status = cartService.applyPromoCode(cartId, promoCode);

        return switch (status) {
            case VALID -> ResponseEntity.ok("Promo code applied successfully!");
            case EXPIRED -> ResponseEntity.badRequest().body("Promo code is expired.");
            case INVALID -> ResponseEntity.badRequest().body("Invalid promo code.");
            default -> ResponseEntity.internalServerError().body("Unexpected error.");
        };
    }
}
