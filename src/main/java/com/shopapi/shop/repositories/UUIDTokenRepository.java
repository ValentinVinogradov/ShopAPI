package com.shopapi.shop.repositories;

import com.shopapi.shop.enums.UUIDTokenType;
import com.shopapi.shop.models.UUIDToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UUIDTokenRepository extends JpaRepository<UUIDToken, Long> {
    Optional<UUIDToken> findUUIDTokenByToken(String token);

    Optional<UUIDToken> findUUIDTokenByUser_Id(UUID userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UUIDToken t WHERE t.user.id = :userId AND t.tokenType = :tokenType")
    void deleteAllTokensWithTypeByUserId(UUID userId, UUIDTokenType tokenType);
}