package com.shopapi.shop.repositories;

import com.shopapi.shop.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
