package com.shopapi.shop.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceUtils {
    public static BigDecimal calculateDiscountedPrice(BigDecimal originalPrice, int discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }

        // Конвертируем discountPercentage в BigDecimal и делим на 100
        BigDecimal discount = BigDecimal.valueOf(discountPercentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // Рассчитываем новую цену
        BigDecimal discountedPrice = originalPrice.subtract(originalPrice.multiply(discount));

        // Возвращаем результат, округленный до 2 знаков
        return discountedPrice.setScale(2, RoundingMode.HALF_UP);
    }
}
