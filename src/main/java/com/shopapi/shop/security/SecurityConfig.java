package com.shopapi.shop.security;

import com.shopapi.shop.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
//@EnableGlobalAuthentication //!
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserLogoutHandler logoutHandler;
    private final JwtFilter jwtFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          UserLogoutHandler logoutHandler,
                          JwtFilter jwtFilter) {
        this.logoutHandler = logoutHandler;
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider customDaoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
//        provider.setUserDetailsService(userDetailsService);
        CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider(
                userDetailsService,
                new BCryptPasswordEncoder(12));
        System.out.println("Установили провайдер");
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Отключаем CSRF для API
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/v1/**",
                                        "/password/v1/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/shop_api/v1/answers/{answerId}",
                                "/shop_api/v1/products/{productId}",
                                "/shop_api/v1/products/all",
                                "/shop_api/v1/products/name/{name}",
                                "/shop_api/v1/products/price-range",
                                "/shop_api/v1/reviews/{reviewId}",
                                "/shop_api/v1/reviews/product/{productId}",
                                "/shop_api/v1/questions/{questionId}",
                                "/shop_api/v1/questions/product/{productId}").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/users/v1/check-signup-token"
                        ).permitAll()
                        .requestMatchers("/admin/v1/**").hasRole("ADMIN") // Доступ для админов
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) //!!!
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // API stateless (без сессий)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(l -> l.logoutUrl("/logout/v1/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler(
                                ((request, response, authentication) -> SecurityContextHolder.clearContext())
                        ))
                .build();
    }
}
