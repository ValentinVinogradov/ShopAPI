package com.shopapi.shop.repository;

import com.shopapi.shop.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    //todo запрос
    List<Question> findQuestionsByProductId(long productId);
    List<Question> findQuestionsByUserId(long userId);
    Optional<Question> findQuestionByUserIdAndProductId(long userId, long productId);
}
