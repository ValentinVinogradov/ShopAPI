package com.shopapi.shop.repositories;

import com.shopapi.shop.models.JWTToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JWTTokenRepository extends JpaRepository<JWTToken, Long> {
    Optional<JWTToken> findJWTTokenByAccessToken(String token);

    Optional<JWTToken> findJWTTokenByRefreshToken(String token);

    List<JWTToken> findAllByUser_Id(UUID userId);

    @Transactional
    @Modifying
//    @Query("DELETE FROM JWTToken t WHERE t.user.id = :userId AND t.isLoggedOut = false")
    @Query("DELETE FROM JWTToken t WHERE t.user.id = :userId")
    void deleteByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM JWTToken t WHERE t.accessToken = :token")
    void deleteByToken(String token);
}
