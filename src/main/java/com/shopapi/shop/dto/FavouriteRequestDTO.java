package com.shopapi.shop.dto;

import java.util.UUID;

/**
 * DTO for {@link com.shopapi.shop.models.Favourite}
 */
public record FavouriteRequestDTO(
        UUID userId,
        UUID productId
) {}