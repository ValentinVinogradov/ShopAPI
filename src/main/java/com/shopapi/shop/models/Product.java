package com.shopapi.shop.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal currentPrice;

    private BigDecimal oldPrice;

    private Integer stockQuantity;

    @Column(nullable = false)
    private LocalDate lastDate;
}
