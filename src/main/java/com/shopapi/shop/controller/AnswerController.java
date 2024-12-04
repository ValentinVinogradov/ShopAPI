package com.shopapi.shop.controller;

import com.shopapi.shop.dto.AnswerRequestDTO;
import com.shopapi.shop.impl.AnswerServiceImpl;
import com.shopapi.shop.models.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop_api/v1/answers")
public class AnswerController extends GenericController<Answer, Long>{
    private final AnswerServiceImpl answerService;

    public AnswerController(AnswerServiceImpl answerService) {
        super(answerService);
        this.answerService = answerService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody AnswerRequestDTO answerRequestDTO) {
        answerService.add(answerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Answer added successfully!");
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody AnswerRequestDTO answerRequestDTO) {
        answerService.update(answerRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Answer updated successfully!");
    }

    @GetMapping
    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@RequestParam long questionId) {
        List<Answer> answers = answerService.getAnswersByQuestionId(questionId);
        if (answers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404, если ответы не найдены
        }
        return ResponseEntity.ok(answers); // 200, если ответы найдены
    }



}
