package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.CartItemResponseDTO;
import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.impl.CartServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/carts")
public class CartController {
    private final CartServiceImpl cartService;

    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartItemResponseDTO>> getCartItemsFromCartByUserId(@PathVariable long userId) {
        try {
            return ResponseEntity.ok(cartService.getCartItemsByUserId(userId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //todo пересмотреть по поводу цен динамических
    @PostMapping("/{cartId}/apply_promo")
    public ResponseEntity<String> applyPromoCode(@PathVariable long cartId, @RequestParam String promoCode) {
        PromoCodeValidationStatus status = cartService.applyPromoCode(cartId, promoCode);

        return switch (status) {
            case VALID -> ResponseEntity.ok("Promo code applied successfully!");
            case EXPIRED -> ResponseEntity.badRequest().body("Promo code is expired.");
            case INVALID -> ResponseEntity.badRequest().body("Invalid promo code.");
        };
    }
}
