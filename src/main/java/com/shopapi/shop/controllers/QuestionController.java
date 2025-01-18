package com.shopapi.shop.controllers;


import com.shopapi.shop.dto.QuestionRequestDTO;
import com.shopapi.shop.dto.QuestionResponseDTO;
import com.shopapi.shop.impl.QuestionServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/questions")
public class QuestionController {
    private final QuestionServiceImpl questionService;

    public QuestionController(QuestionServiceImpl questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> getQuestionById(@PathVariable("id") long questionId) {
        try {
            QuestionResponseDTO questionResponseDTO = questionService.getQuestionById(questionId);
            return ResponseEntity.status(HttpStatus.OK).body(questionResponseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByProductId(@PathVariable long productId) {
        try {
            List<QuestionResponseDTO> questions = questionService.getQuestionsByProductId(productId);
            return ResponseEntity.ok(questions);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Возврат 404, если нет вопросов
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByUserId(@PathVariable long userId) {
        try {
            List<QuestionResponseDTO> questions = questionService.getQuestionsByUserId(userId);
            return ResponseEntity.ok(questions); // Возврат 200, если вопросы найдены
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Возврат 404, если нет вопросов
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/")
    public ResponseEntity<String> addQuestion(@RequestBody QuestionRequestDTO questionRequestDTO) {
        try {
            questionService.addQuestion(questionRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Question added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add question: " + e.getMessage());
        }
    }

    @PutMapping("/")
    public ResponseEntity<String> updateQuestion(@RequestBody QuestionRequestDTO questionRequestDTO) {
        try {
            questionService.updateQuestion(questionRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Question updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update question: " + e.getMessage());
        }
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteQuestionById(@PathVariable long questionId) {
        try {
            questionService.deleteQuestionById(questionId);
            return ResponseEntity.status(HttpStatus.OK).body("Question deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete question: " + e.getMessage());
        }
    }

//todo понять что с этой хуйней делать

//    @GetMapping("/answers/{questionId}")
//    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@PathVariable long questionId) {
//        List<Answer> answers = questionService.getAnswersByQuestionId(questionId);
//        if (answers.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Возврат 404, если нет ответов
//        }
//        return ResponseEntity.ok(answers); // Возврат 200, если ответы найдены
//    }
}
