package com.shopapi.shop.dto;

import com.shopapi.shop.models.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for {@link Product}
 */

public record ProductSaveDTO(
        String name,
        String category,
        String description,
        BigDecimal price,
        List<String> img,
        UUID groupId,
        Integer stockQuantity,
        Map<String, Object> attributes
) {}
