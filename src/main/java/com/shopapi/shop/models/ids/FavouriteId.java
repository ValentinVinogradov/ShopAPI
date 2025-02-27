package com.shopapi.shop.models.ids;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor  // ⬅ Автоматически создаёт конструктор без аргументов (нужен для JPA)
@AllArgsConstructor
@EqualsAndHashCode
public class FavouriteId {
    private UUID userId;
    private UUID productId;
}
