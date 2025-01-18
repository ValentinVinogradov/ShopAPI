package com.shopapi.shop.dto;

import com.shopapi.shop.models.Product;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link Product}
 */
@Value
public class ProductResponseDTO {
    String name;
    String category;
    String description;
    BigDecimal price;
    BigDecimal oldPrice;
    Map<String, Object> attributes;
    List<List<String>> img;
}