package com.shopapi.shop.impl;

import com.shopapi.shop.dto.ReviewRequestDTO;
import com.shopapi.shop.models.Product;
import com.shopapi.shop.models.Review;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repository.ProductRepository;
import com.shopapi.shop.repository.ReviewRepository;
import com.shopapi.shop.repository.UserRepository;
import com.shopapi.shop.services.AbstractService;
import com.shopapi.shop.services.ReviewService;
import com.shopapi.shop.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl extends AbstractService<Review, Long> implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             ProductRepository productRepository) {
        super(reviewRepository);
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public void add(ReviewRequestDTO reviewRequestDTO) {
        //todo обработка исключений

        User user = userRepository.findById(reviewRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Product product = productRepository.findById(reviewRequestDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Review review = new Review();
        review.setDate(DateUtils.getCurrentDateTime());
        review.setUser(user);
        review.setRating(reviewRequestDTO.getRating());
        review.setProduct(product);
        review.setComment(reviewRequestDTO.getComment());

        // Сохраняем в базу
        reviewRepository.save(review);
    }

    public void update(ReviewRequestDTO reviewRequestDTO ) {
        Long userId = reviewRequestDTO.getUserId();
        Long productId = reviewRequestDTO.getProductId();
        Review exsistingReview = reviewRepository.findByUserIdAndProductId(userId, productId).orElseThrow(() -> new RuntimeException("Review not found"));
        exsistingReview.setComment(reviewRequestDTO.getComment());
        exsistingReview.setDate(DateUtils.getCurrentDateTime());
        exsistingReview.setRating(reviewRequestDTO.getRating());
        reviewRepository.save(exsistingReview);

    }


    @Override
    public List<Review> getReviewsByProductId(long productId) {
        return reviewRepository.getReviewsByProductId(productId);
    }

    @Override
    public List<Review> getReviewsByUserId(long userId) {
        return reviewRepository.getReviewsByUserId(userId);
    }


}
