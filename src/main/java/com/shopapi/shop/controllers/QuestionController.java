package com.shopapi.shop.controllers;


import com.shopapi.shop.dto.QuestionRequestDTO;
import com.shopapi.shop.dto.QuestionResponseDTO;
import com.shopapi.shop.impl.QuestionServiceImpl;
import com.shopapi.shop.models.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shop_api/v1/questions")
public class QuestionController {
    private final QuestionServiceImpl questionService;

    public QuestionController(QuestionServiceImpl questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionResponseDTO> getQuestionById(@PathVariable long questionId) {
        try {
            QuestionResponseDTO questionResponseDTO = questionService.getQuestionById(questionId);
            return ResponseEntity.ok(questionResponseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByProductId(@PathVariable UUID productId) {
        try {
            List<QuestionResponseDTO> questions = questionService.getQuestionsByProductId(productId);
            return ResponseEntity.ok(questions);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Возврат 404, если нет вопросов
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByUserId(
            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            List<QuestionResponseDTO> questions = questionService.getQuestionsByUserId(principal.getId());
            return ResponseEntity.ok(questions); // Возврат 200, если вопросы найдены
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Возврат 404, если нет вопросов
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody QuestionRequestDTO questionRequestDTO) {
        try {
            questionService.addQuestion(principal.getId(), questionRequestDTO);
            URI uri = new URI("/questions");
            return ResponseEntity.created(uri).body("Question added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add question: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateQuestion(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody QuestionRequestDTO questionRequestDTO) {
        try {
            questionService.updateQuestion(principal.getId(), questionRequestDTO);
            return ResponseEntity.ok("Question updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update question: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteQuestionById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID productId) {
        try {
            questionService.deleteQuestionById(principal.getId(), productId);
            return ResponseEntity.ok("Question deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete question: " + e.getMessage());
        }
    }
}
