package com.shopapi.shop.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link com.shopapi.shop.models.Product}
 */
@Value
public class ProductRequestDTO {
    String name;
    String category;
    String description;
    BigDecimal price;
    BigDecimal oldPrice;
    Map<String, Object> attributes;
    List<List<String>> img;
}