package com.shopapi.shop.controller;


import com.shopapi.shop.dto.QuestionRequestDTO;
import com.shopapi.shop.impl.QuestionServiceImpl;
import com.shopapi.shop.models.Answer;
import com.shopapi.shop.models.Question;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/questions")
public class QuestionController extends GenericController<Question, Long> {
    private final QuestionServiceImpl questionService;

    public QuestionController(QuestionServiceImpl questionService) {
        super(questionService);
        this.questionService = questionService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody QuestionRequestDTO questionRequestDTO) {
        questionService.add(questionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Question added successfully!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody QuestionRequestDTO questionRequestDTO) {
        questionService.update(questionRequestDTO);
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

    @GetMapping("/answers/{questionId}")
    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@PathVariable long questionId) {
        List<Answer> answers = questionService.getAnswersByQuestionId(questionId);
        if (answers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Возврат 404, если нет ответов
        }
        return ResponseEntity.ok(answers); // Возврат 200, если ответы найдены
    }
}
