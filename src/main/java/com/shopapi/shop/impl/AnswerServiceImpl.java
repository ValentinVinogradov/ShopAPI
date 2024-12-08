package com.shopapi.shop.impl;

import com.shopapi.shop.dto.AnswerRequestDTO;
import com.shopapi.shop.models.Answer;
import com.shopapi.shop.models.Question;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repository.AnswerRepository;
import com.shopapi.shop.repository.QuestionRepository;
import com.shopapi.shop.repository.UserRepository;
import com.shopapi.shop.services.AbstractService;
import com.shopapi.shop.services.AnswerService;
import com.shopapi.shop.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl extends AbstractService<Answer, Long> implements AnswerService {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository,
                             UserRepository userRepository,
                             QuestionRepository questionRepository) {
        super(answerRepository);
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @Transactional
    @Override
    public void addAnswer(AnswerRequestDTO answerRequestDTO) {
        User user = userRepository.findById(answerRequestDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Question question = questionRepository.findById((answerRequestDTO.getQuestionId())).orElseThrow(() -> new EntityNotFoundException("Question not found"));
        Answer answer = new Answer();
        answer.setUser(user);
        answer.setQuestion(question);
        answer.setUsername(answerRequestDTO.getUsername() != null ? answerRequestDTO.getUsername() : "Anonymous");
        answer.setContent(answerRequestDTO.getContent());
        answer.setDate(DateUtils.getCurrentDate());
        answerRepository.save(answer);
    }

    @Transactional
    @Override
    public void updateAnswer(AnswerRequestDTO answerRequestDTO) {
        Long userId = answerRequestDTO.getUserId();
        Long questionId = answerRequestDTO.getQuestionId();
        Answer exsistingAnswer = answerRepository.findByUserIdAndQuestionId(userId, questionId).orElseThrow(() -> new EntityNotFoundException("Answer not found"));
        exsistingAnswer.setContent(answerRequestDTO.getContent());
        exsistingAnswer.setDate(DateUtils.getCurrentDate());
        answerRepository.save(exsistingAnswer);
    }

    @Override
    public List<Answer> getAnswersByQuestionId(long questionId) {
        return answerRepository.findAnswersByQuestionId(questionId);
    }
}
