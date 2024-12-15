package com.shopapi.shop.controller;

import com.shopapi.shop.dto.AnswerRequestDTO;
import com.shopapi.shop.dto.AnswerResponseDTO;
import com.shopapi.shop.impl.AnswerServiceImpl;
import com.shopapi.shop.models.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        AnswerResponseDTO answerResponseDTO = answerService.getAnswerById(answerId);
        return ResponseEntity.status(HttpStatus.OK).body(answerResponseDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AnswerResponseDTO>> getAnswersByUserId(@PathVariable long userId) {
        List<AnswerResponseDTO> answerResponseDTOs = answerService.getAnswersByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(answerResponseDTOs);
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
    public ResponseEntity<String> add(@RequestBody AnswerRequestDTO answerRequestDTO) {
        answerService.addAnswer(answerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Answer added successfully!");
    }

    @PutMapping("/")
    public ResponseEntity<String> update(@RequestBody AnswerRequestDTO answerRequestDTO) {
        answerService.updateAnswer(answerRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Answer updated successfully!");
    }


    @DeleteMapping("/{answerId}")
    public ResponseEntity<String> deleteAnswerById(@PathVariable long answerId) {
        answerService.deleteAnswerById(answerId);
        return ResponseEntity.status(HttpStatus.OK).body("Answer deleted successfully!");
    }
}
