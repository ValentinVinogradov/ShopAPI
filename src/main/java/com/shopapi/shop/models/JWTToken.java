package com.shopapi.shop.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "jwt_tokens")
@ToString
@Getter
@Setter
public class JWTToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //todo дописать потом эту инфу скорее всего это будет мап
//    private String device;

//    @Column(name = "logged_out")
//    private boolean isLoggedOut;
}
