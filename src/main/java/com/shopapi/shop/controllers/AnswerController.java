package com.shopapi.shop.controllers;

import com.shopapi.shop.dto.AnswerRequestDTO;
import com.shopapi.shop.dto.AnswerResponseDTO;
import com.shopapi.shop.impl.AnswerServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/answers")
public class AnswerController{

    private final AnswerServiceImpl answerService;

    public AnswerController(AnswerServiceImpl answerService) {
        this.answerService = answerService;
    }


    @GetMapping("/{answerId}")
    public ResponseEntity<AnswerResponseDTO> getAnswerById(@PathVariable long answerId) {
        try {
            AnswerResponseDTO answerResponseDTO = answerService.getAnswerById(answerId);
            return ResponseEntity.ok(answerResponseDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AnswerResponseDTO>> getAnswersByUserId(@PathVariable long userId) {
        try{
            List<AnswerResponseDTO> answerResponseDTOs = answerService.getAnswersByUserId(userId);
            return ResponseEntity.ok(answerResponseDTOs);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

//    @GetMapping("/question/{questionId}")
//    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@PathVariable long questionId) {
//        List<Answer> answers = answerService.getAnswersByQuestionId(questionId);
//        if (answers.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404, если ответы не найдены
//        }
//        return ResponseEntity.ok(answers); // 200, если ответы найдены
//    }

    @PostMapping("/")
    public ResponseEntity<String> addAnswer(@RequestBody AnswerRequestDTO answerRequestDTO) {
        try {
            answerService.addAnswer(answerRequestDTO);
            URI uri = new URI("/shop_api/v1/answers");
            return ResponseEntity.created(uri).body("Answer added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add answer: " + e.getMessage());
        }

    }

    @PutMapping("/")
    public ResponseEntity<String> updateAnswer(@RequestBody AnswerRequestDTO answerRequestDTO) {
        try {
            answerService.updateAnswer(answerRequestDTO);
            return ResponseEntity.ok("Answer updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update answer: " + e.getMessage());
        }
    }


    @DeleteMapping("/{answerId}")
    public ResponseEntity<String> deleteAnswerById(@PathVariable long answerId) {
        try {
            answerService.deleteAnswerById(answerId);
            return ResponseEntity.ok("Answer deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete answer: " + e.getMessage());
        }
    }
}
