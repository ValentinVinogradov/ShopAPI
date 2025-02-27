package com.shopapi.shop.repositories;

import com.shopapi.shop.models.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    List<Favourite> findAllByUser_Id(UUID userId);
    List<Favourite> findAllByProduct_Id(UUID productId);

    @Transactional
    @Modifying
    void deleteByUser_IdAndProduct_Id(UUID userId, UUID productId);
}
