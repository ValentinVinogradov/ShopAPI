package com.shopapi.shop.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OidcUserServiceImpl extends OidcUserService {
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        // Загружаем пользователя через стандартный OidcUserService
        OidcUser oidcUser = super.loadUser(userRequest);

        // Добавляем роль ROLE_USER
        List<GrantedAuthority> authorities = new ArrayList<>(oidcUser.getAuthorities());
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Возвращаем пользователя с дополнительной ролью
        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
