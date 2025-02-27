package com.shopapi.shop.dto;

import com.shopapi.shop.models.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * DTO for {@link Product}
 */
public record ProductCatalogDTO (
    String name,
    BigDecimal price,
    BigDecimal oldPrice,
    List<String> img,
    Integer discountPercentage,
    Integer reviewCount,
    Double rating
) {}