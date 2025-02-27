package com.shopapi.shop.dto;

import java.util.UUID;

/**
 * DTO for {@link com.shopapi.shop.models.Favourite}
 */
public record FavouriteResponseDTO(
        UUID userId,
        UUID productId
) {}