package com.shopapi.shop.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class Oauth2UserServiceImpl extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("Зашли в сервис");

        // Добавляем роли
        Set<GrantedAuthority> updatedAuthorities = new HashSet<>(oAuth2User.getAuthorities());
        updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // Возвращаем нового пользователя с дополнительной ролью
        return new DefaultOAuth2User(
                updatedAuthorities,
                oAuth2User.getAttributes(),
                userNameAttributeName  // Или другой ключ, который идентифицирует пользователя
        );
    }
}
