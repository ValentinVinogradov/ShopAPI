package com.shopapi.shop.repository;

import com.shopapi.shop.models.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    List<Favourite> findAllByUser_Id(Long id);

    @Transactional
    @Modifying
    @Query("delete from Favourite f where user.id=:userId and product.id=:productId")
    void deleteByUserAndProduct(long userId, long productId);
}
