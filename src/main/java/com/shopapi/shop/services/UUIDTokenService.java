package com.shopapi.shop.services;

import com.shopapi.shop.models.UUIDToken;
import com.shopapi.shop.models.User;


public interface UUIDTokenService {
    UUIDToken generateToken(User user);
}
