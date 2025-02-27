package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.CartResponseDTO;
import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.impl.CartServiceImpl;
import com.shopapi.shop.models.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/shop_api/v1/carts")
public class CartController {
    private final CartServiceImpl cartService;

    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }


    @GetMapping("/get-cart")
    public ResponseEntity<CartResponseDTO> getCartByUserId(
            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            return ResponseEntity.ok(cartService.getCartById(principal.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    //todo пересмотреть по поводу цен динамических
    @PostMapping("/{cartId}/apply_promo")
    public ResponseEntity<String> applyPromoCode(@PathVariable UUID cartId, @RequestParam String promoCode) {
        PromoCodeValidationStatus status = cartService.applyPromoCode(cartId, promoCode);

        return switch (status) {
            case VALID -> ResponseEntity.ok("Promo code applied successfully!");
            case EXPIRED -> ResponseEntity.badRequest().body("Promo code is expired.");
            case INVALID -> ResponseEntity.badRequest().body("Invalid promo code.");
        };
    }
}
