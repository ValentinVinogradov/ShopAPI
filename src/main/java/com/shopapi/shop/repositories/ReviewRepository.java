package com.shopapi.shop.repositories;

import com.shopapi.shop.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findReviewsByProduct_Id(long productId);
    List<Review> findReviewsByUser_Id(long userId);

//    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.product.id = :productId")
    Optional<Review> findReviewByUser_IdAndProduct_Id(@Param("userId") Long userId, @Param("productId") Long productId);
}
