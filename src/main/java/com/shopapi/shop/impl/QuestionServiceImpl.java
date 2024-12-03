package com.shopapi.shop.impl;

import com.shopapi.shop.models.Answer;
import com.shopapi.shop.models.Question;
import com.shopapi.shop.repository.QuestionRepository;
import com.shopapi.shop.services.AbstractService;
import com.shopapi.shop.services.QuestionService;
import com.shopapi.shop.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl extends AbstractService<Question, Long> implements QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerServiceImpl answerService;

    public QuestionServiceImpl(QuestionRepository questionRepository,
                               AnswerServiceImpl answerService) {
        super(questionRepository);
        this.questionRepository = questionRepository;
        this.answerService = answerService;
    }

    @Override
    public void add(Question question) {
        question.setDate(DateUtils.getCurrentDate());
        questionRepository.save(question);
    }

    @Override
    public void update(Question question) {
        question.setDate(DateUtils.getCurrentDate());
        questionRepository.save(question);
    }


    @Override
    public List<Question> getQuestionsByProductId(long productId) {
        return questionRepository.findQuestionsByProductId(productId);
    }

    @Override
    public List<Question> getQuestionsByUserId(long userId) {
        return questionRepository.findQuestionsByUserId(userId);
    }

    @Override
    public List<Answer> getAnswersByQuestionId(long questionId) {
        return answerService.getAnswersByQuestionId(questionId);
    }

}
