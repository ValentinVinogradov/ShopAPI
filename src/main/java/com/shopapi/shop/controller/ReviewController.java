package com.shopapi.shop.controller;

import com.shopapi.shop.dto.ReviewRequestDTO;
import com.shopapi.shop.impl.ReviewServiceImpl;
import com.shopapi.shop.models.Review;
import com.shopapi.shop.repository.ProductRepository;
import com.shopapi.shop.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/reviews")
public class ReviewController extends GenericController<Review, Long> {
    private final ReviewServiceImpl reviewService;


    public ReviewController(ReviewServiceImpl reviewService,
                            UserRepository userRepository,
                            ProductRepository productRepository) {
        super(reviewService);
        this.reviewService = reviewService;

    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        reviewService.add(reviewRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Review added successful!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        reviewService.update(reviewRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Review updated successful!");
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable long productId) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);

        if (reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 если список пуст
        }
        return ResponseEntity.ok(reviews); // 200 если данные найдены
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable long userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);

        if (reviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 если список пуст
        }
        return ResponseEntity.ok(reviews); // 200 если данные найдены
    }
}
