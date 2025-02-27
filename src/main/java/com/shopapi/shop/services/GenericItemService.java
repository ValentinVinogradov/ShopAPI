package com.shopapi.shop.services;

import com.shopapi.shop.models.OrderItem;

import java.util.List;
import java.util.UUID;

public interface GenericItemService<T, ID> extends GenericService<T, ID>{
    List<T> getItemsByContainerId(ID containerId);       // Получить все элементы в контейнере
    void deleteAllItemsByContainerId(ID containerId);    // Удалить все элементы из контейнера
    void updateQuantity(ID itemId, int quantity);        // Обновить количество элемента
    boolean isProductInContainer(ID containerId, UUID productId);  // Проверить, есть ли товар в контейнере
}


