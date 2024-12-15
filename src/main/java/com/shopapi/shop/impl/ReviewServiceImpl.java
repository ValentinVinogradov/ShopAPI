package com.shopapi.shop.impl;

import com.shopapi.shop.dto.ReviewRequestDTO;
import com.shopapi.shop.dto.ReviewResponseDTO;
import com.shopapi.shop.dto.UserResponseDTO;
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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ReviewResponseDTO getReviewById(long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not found"));
        User user = review.getUser();
        return new ReviewResponseDTO(review.getId(),
                review.getProduct().getId(),
                new UserResponseDTO(user.getId(), user.getUsername()),
                review.getUsername(),
                review.getRating(),
                review.getDignities(),
                review.getFlaws(),
                review.getContent(),
                review.getDate());
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByProductId(long productId) {
        List<Review> reviews = reviewRepository.findReviewsByProduct_Id(productId);
        return reviews.stream()
                .map(review -> new ReviewResponseDTO(review.getId(),
                        review.getProduct().getId(),
                        new UserResponseDTO(review.getUser().getId(), review.getUser().getUsername()),
                        review.getUsername(),
                        review.getRating(),
                        review.getDignities(),
                        review.getFlaws(),
                        review.getContent(),
                        review.getDate()))
                .toList();
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByUserId(long userId) {
        List<Review> reviews = reviewRepository.findReviewsByUser_Id(userId);
        User user = userRepository.findById(userId).
                orElseThrow(() -> new EntityNotFoundException("User not found"));
        return reviews.stream()
                .map(review -> new ReviewResponseDTO(review.getId(),
                        review.getProduct().getId(),
                        new UserResponseDTO(user.getId(), user.getUsername()),
                        review.getUsername(),
                        review.getRating(),
                        review.getDignities(),
                        review.getFlaws(),
                        review.getContent(),
                        review.getDate()))
                .toList();
    }

    @Transactional
    @Override
    public void addReview(ReviewRequestDTO reviewRequestDTO) {
        //todo обработка исключений
        User user = userRepository.findById(reviewRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Product product = productRepository.findById(reviewRequestDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(reviewRequestDTO.getRating());
        review.setContent(reviewRequestDTO.getContent());
        review.setUsername(user.getUsername());
        review.setDignities(reviewRequestDTO.getDignities());
        review.setFlaws(reviewRequestDTO.getFlaws());
        review.setDate(DateUtils.getCurrentDate());

        System.out.println(review);
        // Сохраняем в базу
        reviewRepository.save(review);
    }

    @Transactional
    @Override
    public void updateReview(ReviewRequestDTO reviewRequestDTO) {
        Long userId = reviewRequestDTO.getUserId();
        Long productId = reviewRequestDTO.getProductId();
        Review exsistingReview = reviewRepository.findByUserIdAndProductId(userId, productId).orElseThrow(() -> new RuntimeException("Review not found"));
        exsistingReview.setContent(reviewRequestDTO.getContent());
        exsistingReview.setRating(reviewRequestDTO.getRating());
        exsistingReview.setDignities(reviewRequestDTO.getDignities());
        exsistingReview.setFlaws(reviewRequestDTO.getFlaws());
        exsistingReview.setDate(DateUtils.getCurrentDate());
        reviewRepository.save(exsistingReview);
    }

    @Override
    public void deleteReviewById(long reviewId) {
        reviewRepository.deleteById(reviewId);
    }


}
