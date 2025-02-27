package com.shopapi.shop.security;

import com.shopapi.shop.impl.UserDetailsServiceImpl;
import com.shopapi.shop.models.UserPrincipal;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomDaoAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomDaoAuthenticationProvider(UserDetailsServiceImpl userDetailsService,
                                           PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String identifier = authentication.getName();
        String password = authentication.getCredentials().toString();


        try {
            UserPrincipal userDetails = new UserPrincipal(userDetailsService.loadUserByIdentifier(identifier));

            System.out.println(userDetails);
            if (!userDetails.isEnabled()) {
                throw new DisabledException("Учётная запись неактивна");
            }
            if (!userDetails.isAccountNonLocked()) {
                throw new LockedException("Учётная запись заблокирована");
            }
            if (!userDetails.isAccountNonExpired()) {
                throw new AccountExpiredException("Учётная запись истекла");
            }
            if (!userDetails.isCredentialsNonExpired()) {
                throw new CredentialsExpiredException("Срок действия учётных данных истёк");
            }

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Неверный пароль");
            }

            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    /**
     * Метод определяет тип идентификатора и загружает пользователя соответствующим способом.
     */
//    private UserPrincipal loadUserByIdentifier(String identifier) {
//        if (isEmail(identifier)) {
//            return userDetailsService.loadUserByEmail(identifier);
//        } else if (isPhoneNumber(identifier)) {
//            //todo телефон
//            return null;
//        } else {
//            // Если не email и не телефон – считаем, что это username
//            return userDetailsService.loadUserByUsername(identifier);
//        }
//    }

    /**
     * Простейшая проверка на email (наличие символа '@').
     */
//    private boolean isEmail(String identifier) {
//        return identifier != null && identifier.contains("@");
//    }
//
//    /**
//     * Простейшая проверка на телефон: строка должна состоять из цифр,
//     * возможно, с ведущим плюсом.
//     */
//    private boolean isPhoneNumber(String identifier) {
//        return identifier != null && identifier.matches("^\\+?\\d+$");
//    }

    @Override
    public boolean supports(Class<?> authentication) {
        // Этот провайдер поддерживает аутентификацию через UsernamePasswordAuthenticationToken
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
