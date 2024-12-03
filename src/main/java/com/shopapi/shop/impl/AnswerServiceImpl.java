package com.shopapi.shop.impl;

import com.shopapi.shop.models.Answer;
import com.shopapi.shop.models.Review;
import com.shopapi.shop.repository.AnswerRepository;
import com.shopapi.shop.services.AbstractService;
import com.shopapi.shop.services.AnswerService;
import com.shopapi.shop.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl extends AbstractService<Answer, Long> implements AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository) {
        super(answerRepository);
        this.answerRepository = answerRepository;
    }

    @Override
    public void add(Answer answer) {
        answer.setDate(DateUtils.getCurrentDate());
        answerRepository.save(answer);
    }

    @Override
    public void update(Answer answer) {
        answer.setDate(DateUtils.getCurrentDate());
        answerRepository.save(answer);
    }


    @Override
    public List<Answer> getAnswersByQuestionId(long questionId) {
        return answerRepository.findAnswersByQuestionId(questionId);
    }
}
