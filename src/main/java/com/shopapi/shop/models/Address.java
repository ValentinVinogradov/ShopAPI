package com.shopapi.shop.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Setter
@Getter
@ToString
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String region;

    private String city;

    private String street;

    private Integer houseNumber;

    private boolean isActive;
}
