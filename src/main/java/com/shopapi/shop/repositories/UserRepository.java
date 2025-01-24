package com.shopapi.shop.repositories;

import com.shopapi.shop.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//todo тут необходимые методы дописать для запросов в бд
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.username = :username WHERE u.id = :id")
    void updateUsername(@Param("id") Long userId, @Param("username") String username);

//    @Modifying
//    @Transactional
//    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
//    void updatePassword(@Param("id") Long userId, @Param("password") String password);
}

