package com.shopapi.shop.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }


    @Override
    public String getPassword() {
        System.out.println("Был вызван метод getPassword");
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        System.out.println("Был вызван метод getUsername");
        return user.getUsername();
    }

    public String getEmail() {
        System.out.println("Был вызван метод getEmail");
        return user.getEmail();
    }

    public String getPhoneNumber() {
        return null;
    }

    public UUID getId() {
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //todo либо это использовать либо свое поле
    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
