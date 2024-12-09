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
import jakarta.transaction.Transactional;
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

    @Transactional
    @Override
    public void addReview(ReviewRequestDTO reviewRequestDTO) {
        Review review = new Review();
        System.out.println(DateUtils.getCurrentDate());
//        //todo обработка исключений
//        System.out.println("зашли в метод добавления отзыва");
//        User user = userRepository.findById(reviewRequestDTO.getUserId())
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//        System.out.println("получили пользователя");
//        Product product = productRepository.findById(reviewRequestDTO.getProductId())
//                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
//        System.out.println("получили товар");
//        Review review = new Review();
//        System.out.println("Создание отзыва пустого");
//        System.out.println(review);
//        review.setUser(user);
//        System.out.println("присвоение пользователя");
//        review.setProduct(product);
//        System.out.println("присвоение товара");
//        review.setRating(reviewRequestDTO.getRating());
//        System.out.println("присвоение рейтинга");
//        review.setContent(reviewRequestDTO.getContent());
//        System.out.println("присвоение содержания");
//        review.setUsername(user.getUsername());
//        System.out.println("присвоение юзернейма");
//        review.setDignities(reviewRequestDTO.getDignities());
//        System.out.println("присвоение достоинств");
//        review.setFlaws(reviewRequestDTO.getFlaws());
//        System.out.println("присвоение недостатков");
//        review.setDate(DateUtils.getCurrentDate());
//        System.out.println("присвоение даты");
//
//        System.out.println("сам отзыв");
//        System.out.println(review);
//        // Сохраняем в базу
//        reviewRepository.save(review);
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
    public List<Review> getReviewsByProductId(long productId) {
        return reviewRepository.getReviewsByProductId(productId);
    }

    @Override
    public List<Review> getReviewsByUserId(long userId) {
        return reviewRepository.getReviewsByUserId(userId);
    }


}
