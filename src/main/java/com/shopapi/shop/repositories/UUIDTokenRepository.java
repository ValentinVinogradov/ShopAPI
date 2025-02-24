package com.shopapi.shop.repositories;

import com.shopapi.shop.enums.UUIDTokenType;
import com.shopapi.shop.models.UUIDToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UUIDTokenRepository extends JpaRepository<UUIDToken, Long> {
    Optional<UUIDToken> findUUIDTokenByToken(String token);

    Optional<UUIDToken> findUUIDTokenByUser_Id(long userId);

//    @Modifying
//    @Transactional
//    @Query("DELETE FROM UUIDToken t WHERE t.user.id = :userId")
//    void deleteUUIDTokenByUser_Id(long userId);


    //todo переделать запросы
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM UUIDToken t WHERE t.user.id = :userId AND t.tokenType = 'EMAIL'")
//    void deleteEmailTypeTokenByUserId(long userId);
//
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM UUIDToken t WHERE t.user.id = :userId AND t.tokenType = 'PASSWORD'")
//    void deletePasswordTypeTokenByUserId(long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UUIDToken t WHERE t.user.id = :userId AND t.tokenType = :tokenType")
    void deleteAllTokensWithTypeByUserId(long userId, UUIDTokenType tokenType);
}