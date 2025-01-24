package com.shopapi.shop.dto;


import lombok.Value;

@Value
public class UserFieldsRequestDTO {
    String email;
    String token;
    String newPassword;
    String newEmail;
}
