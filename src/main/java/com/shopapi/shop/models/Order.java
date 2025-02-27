package com.shopapi.shop.models;

import com.shopapi.shop.enums.OrderPaymentStatus;
import com.shopapi.shop.enums.OrderStatus;
import com.shopapi.shop.services.OrderService;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    @ToString.Exclude
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "status",nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "payment_status", nullable = false) //todo ? надо ли нуллабл
    private OrderPaymentStatus orderPaymentStatus;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "total_count", nullable = false)
    private Integer totalCount;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_cost")
    private BigDecimal shippingCost;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "tracking_number")
    private String trackingNumber;


//    @Column(name = "payment_method")
//    private String paymentMethod;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;



    //todo всякие доп поля оплата статус оплаты промокод и сертификат
    // тип службы доставки стоимость доставки если не включено в стоимость
    // примерная дата доставки

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Order order = (Order) o;
        return getId() != null && Objects.equals(getId(), order.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

