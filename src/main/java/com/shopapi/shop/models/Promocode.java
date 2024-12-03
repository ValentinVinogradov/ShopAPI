package com.shopapi.shop.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "promocodes")
@Data
public class Promocode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // Уникальный код промокода

    @Column(nullable = false)
    private int discountPercentage; // Скидка в процентах

    @Column
    private LocalDateTime startDate; // Дата начала действия

    @Column
    private LocalDateTime endDate; // Дата окончания действия

    @Column
    private Integer usageLimit; // Лимит использования (null = неограниченный)

    //todo дописать
    @Column
    private Integer usedCount = 0; // Количество использований
}