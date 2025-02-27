package com.shopapi.shop.services;

import com.shopapi.shop.dto.ReviewRequestDTO;
import com.shopapi.shop.dto.ReviewResponseDTO;
import com.shopapi.shop.models.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewService{
    ReviewResponseDTO getReviewById(long reviewId);
    List<ReviewResponseDTO> getReviewsByProductId(UUID productId);
    List<ReviewResponseDTO> getReviewsByUserId(UUID userId);
    void addReview(UUID userId, ReviewRequestDTO reviewRequestDTO);
    void updateReview(UUID userId, ReviewRequestDTO reviewRequestDTO);
    void deleteReviewById(UUID userId, UUID productId);
}
