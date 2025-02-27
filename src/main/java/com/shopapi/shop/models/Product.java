package com.shopapi.shop.models;

import jakarta.persistence.*;
import lombok.*;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "price",nullable = false)
    private BigDecimal price;

    @Column(name = "old_price")
    private BigDecimal oldPrice;

    @Column(name = "discount_percentage")
    private Integer discountPercentage;

    @Column(name = "review_count")
    private Integer reviewCount;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "stock_quantity",nullable = false)
    private Integer stockQuantity;

    @Column(name = "group_id", nullable = false)
    private UUID groupId;

//     Поле для хранения JSON-атрибутов
    @Column(name = "attributes", columnDefinition = "JSONB", nullable = false)
    @Type(JsonType.class)
    private Map<String, Object> attributes = new HashMap<>();

    @Column(name = "img", nullable = false)
    private List<String> img;

    @Column(name = "last_date")
    private LocalDate lastDate;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
