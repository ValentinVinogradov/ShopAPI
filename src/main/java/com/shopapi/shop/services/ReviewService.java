package com.shopapi.shop.services;

import com.shopapi.shop.dto.ReviewRequestDTO;
import com.shopapi.shop.dto.ReviewResponseDTO;
import com.shopapi.shop.models.Review;

import java.util.List;

public interface ReviewService{
    ReviewResponseDTO getReviewById(long reviewId);
    List<ReviewResponseDTO> getReviewsByProductId(long productId);
    List<ReviewResponseDTO> getReviewsByUserId(long userId);
    void addReview(ReviewRequestDTO reviewRequestDTO);
    void updateReview(ReviewRequestDTO reviewRequestDTO);
    void deleteReviewById(long reviewId);
}
