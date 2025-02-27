package com.shopapi.shop.dto;

/**
 * DTO for {@link com.shopapi.shop.models.Address}
 */

public record AddressRequestDTO (
        String region,
        String city,
        String street,
        Integer houseNumber
) {}