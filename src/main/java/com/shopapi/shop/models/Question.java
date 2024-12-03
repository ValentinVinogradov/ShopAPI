package com.shopapi.shop.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "questions")
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "question")
    private Answer answer;
}
