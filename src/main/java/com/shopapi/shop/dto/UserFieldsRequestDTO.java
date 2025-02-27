package com.shopapi.shop.dto;



public record UserFieldsRequestDTO(
        String email,
        String token,
        String newPassword,
        String newUsername,
        String newEmail) {
}
