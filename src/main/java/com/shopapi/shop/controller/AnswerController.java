package com.shopapi.shop.controller;

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

    @GetMapping
    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@RequestParam long questionId) {
        List<Answer> answers = answerService.getAnswersByQuestionId(questionId);
        if (answers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404, если ответы не найдены
        }
        return ResponseEntity.ok(answers); // 200, если ответы найдены
    }



}
