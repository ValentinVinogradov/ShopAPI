package com.shopapi.shop.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtils {
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
