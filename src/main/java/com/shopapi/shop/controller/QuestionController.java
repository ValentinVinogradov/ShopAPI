package com.shopapi.shop.controller;


import com.shopapi.shop.dto.QuestionRequestDTO;
import com.shopapi.shop.dto.QuestionResponseDTO;
import com.shopapi.shop.impl.QuestionServiceImpl;
import com.shopapi.shop.models.Question;
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
    public ResponseEntity<QuestionResponseDTO> getQuestionDTOById(@PathVariable("id") long questionId) {
        QuestionResponseDTO questionResponseDTO = questionService.getQuestionById(questionId);
        return ResponseEntity.status(HttpStatus.OK).body(questionResponseDTO);
    }

    @PostMapping("/")
    public ResponseEntity<String> addQuestion(@RequestBody QuestionRequestDTO questionRequestDTO) {
        questionService.addQuestion(questionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Question added successfully!");
    }

    @PutMapping("/")
    public ResponseEntity<String> update(@RequestBody QuestionRequestDTO questionRequestDTO) {
        questionService.updateQuestion(questionRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Question updated successfully!");
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Question>> getQuestionsByProductId(@PathVariable long productId) {
        List<Question> questions = questionService.getQuestionsByProductId(productId);
        if (questions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Возврат 404, если нет вопросов
        }
        return ResponseEntity.ok(questions); // Возврат 200, если вопросы найдены
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Question>> getQuestionsByUserId(@PathVariable long userId) {
        List<Question> questions = questionService.getQuestionsByUserId(userId);
        if (questions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Возврат 404, если нет вопросов
        }
        return ResponseEntity.ok(questions); // Возврат 200, если вопросы найдены
    }

//    @GetMapping("/answers/{questionId}")
//    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@PathVariable long questionId) {
//        List<Answer> answers = questionService.getAnswersByQuestionId(questionId);
//        if (answers.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Возврат 404, если нет ответов
//        }
//        return ResponseEntity.ok(answers); // Возврат 200, если ответы найдены
//    }
}
