package com.shopapi.shop.repositories;

import com.shopapi.shop.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByProduct_Id(UUID productId);
    List<Review> findAllByUser_Id(UUID userId);

//    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.product.id = :productId")
    Optional<Review> findByUser_IdAndProduct_Id(UUID userId, UUID productId);

    void deleteByUser_IdAndProduct_Id(UUID userId, UUID productId);
}
