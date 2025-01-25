package com.shopapi.shop.models;

import com.shopapi.shop.enums.UUIDTokenType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Table(name = "uuid_tokens")
@Entity
@Setter
@Getter
public class UUIDToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private UUIDTokenType tokenType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expires_at")
    private Date expiresAt;
}
