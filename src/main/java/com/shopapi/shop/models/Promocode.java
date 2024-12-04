package com.shopapi.shop.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "promocodes")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Promocode promocode = (Promocode) o;
        return getId() != null && Objects.equals(getId(), promocode.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}