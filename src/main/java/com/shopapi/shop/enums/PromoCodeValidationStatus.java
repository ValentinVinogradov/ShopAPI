package com.shopapi.shop.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PromoCodeValidationStatus {
    INVALID("Promo code is invalid"),       // Промокод неверный
    EXPIRED("Promo code has expired"),     // Промокод истек
    VALID("Promo code is valid");          // Промокод действителен

    private final String message;
}
