package com.shopapi.shop.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ResetPasswordRequestDTO {
    String email;
    String token;
    String newPassword;
}
