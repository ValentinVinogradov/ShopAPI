package com.shopapi.shop.services;

import com.shopapi.shop.models.Review;

import java.util.List;

public interface ReviewService extends GenericService<Review, Long> {
    List<Review> getReviewsByProductId(long productId);
    List<Review> getReviewsByUserId(long userId);
}
