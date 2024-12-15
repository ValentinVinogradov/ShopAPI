package com.shopapi.shop.repository;

import com.shopapi.shop.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findQuestionsByProduct_Id(long productId);
    List<Question> findQuestionsByUser_Id(long userId);
    Optional<Question> findQuestionByUserIdAndProductId(long userId, long productId);
}
