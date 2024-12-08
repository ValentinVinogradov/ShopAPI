package com.shopapi.shop.impl;

import com.shopapi.shop.dto.QuestionRequestDTO;
import com.shopapi.shop.models.Answer;
import com.shopapi.shop.models.Product;
import com.shopapi.shop.models.Question;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repository.ProductRepository;
import com.shopapi.shop.repository.QuestionRepository;
import com.shopapi.shop.repository.UserRepository;
import com.shopapi.shop.services.AbstractService;
import com.shopapi.shop.services.QuestionService;
import com.shopapi.shop.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl extends AbstractService<Question, Long> implements QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerServiceImpl answerService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository,
                               AnswerServiceImpl answerService,
                               UserRepository userRepository,
                               ProductRepository productRepository) {
        super(questionRepository);
        this.questionRepository = questionRepository;
        this.answerService = answerService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public void addQuestion(QuestionRequestDTO questionRequestDTO) {
        User user = userRepository.findById(questionRequestDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Product product = productRepository.findById(questionRequestDTO.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        Question question = new Question();
        question.setUser(user);
        question.setProduct(product);
        question.setContent(questionRequestDTO.getContent());
        question.setUsername(questionRequestDTO.getUsername());
        question.setDate(DateUtils.getCurrentDate());
        questionRepository.save(question);
    }

    @Transactional
    @Override
    public void updateQuestion(QuestionRequestDTO questionRequestDTO) {
        Long userId = questionRequestDTO.getUserId();
        Long productId = questionRequestDTO.getProductId();
        Question exsistingQuestion = questionRepository.findQuestionByUserIdAndProductId(userId, productId).orElseThrow(() -> new EntityNotFoundException("Question not found"));
        exsistingQuestion.setContent(questionRequestDTO.getContent());
        exsistingQuestion.setDate(DateUtils.getCurrentDate());
        questionRepository.save(exsistingQuestion);
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
