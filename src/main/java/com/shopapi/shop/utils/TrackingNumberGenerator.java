package com.shopapi.shop.utils;
import java.util.concurrent.ThreadLocalRandom;

public class TrackingNumberGenerator {
    public static String generateTrackingNumber() {
        String prefix = "SA";

        // Генерация числа из 10 цифр (от 1000000000 до 9999999999)
        long randomNumber = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);

        return prefix + randomNumber;
    }
}
