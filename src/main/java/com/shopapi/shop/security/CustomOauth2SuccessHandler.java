package com.shopapi.shop.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopapi.shop.dto.JWTTokenResponseDTO;
import com.shopapi.shop.impl.JWTServiceImpl;
import com.shopapi.shop.impl.RoleServiceImpl;
import com.shopapi.shop.impl.UserServiceImpl;
import com.shopapi.shop.models.Role;
import com.shopapi.shop.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOauth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final JWTServiceImpl jwtService;

    public CustomOauth2SuccessHandler(UserServiceImpl userService,
                                 RoleServiceImpl roleService,
                                 JWTServiceImpl jwtService) {
        this.userService = userService;
        this.roleService = roleService;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println(oauth2User);


        String oauth2Id = null;
        String username = null;
        String email = null;
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = authenticationToken.getAuthorizedClientRegistrationId();
        switch (registrationId) {
            case "google":
                email = (String) oauth2User.getAttributes().get("email");
                username = email.split("@")[0];
                oauth2Id = (String) oauth2User.getAttributes().get("sub");
                break;
            case "yandex":
                email = (String) oauth2User.getAttributes().get("default_email");
                oauth2Id = (String) oauth2User.getAttributes().get("id");
                username = (String) oauth2User.getAttributes().get("login");
                break;
        }
        User exsistingUser = userService.getUserByOauth2Id(oauth2Id);
        Role userRole = roleService.getRoleByName("USER");
        if (exsistingUser == null) {
            User newUser = userService.createUser(username, email);
            newUser.getRoles().add(userRole);
            newUser.setEmailConfirmed(true);
            newUser.setOauth2Id(oauth2Id);
            newUser.setActive(true);
            exsistingUser = newUser;
        }

        userService.saveUser(exsistingUser);

        jwtService.revokeAllUserAccessTokens(exsistingUser);
        JWTTokenResponseDTO jwtTokens = jwtService.generateJwtTokens(exsistingUser);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(jwtTokens));
//        response.sendRedirect("/oauth/v1/success?access_token=" + jwtTokens.accessToken()
//                + "&refresh_token=" + jwtTokens.refreshToken());
    }
}

