package com.shopapi.shop.repositories;

import com.shopapi.shop.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByProduct_Id(UUID productId);
    List<Question> findAllByUser_Id(UUID userId);
    Optional<Question> findByUser_IdAndProduct_Id(UUID userId, UUID productId);
    void deleteByUser_IdAndProduct_Id(UUID userId, UUID productId);
}
