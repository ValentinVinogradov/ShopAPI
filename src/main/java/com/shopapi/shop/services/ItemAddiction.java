package com.shopapi.shop.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemAddiction {
    public <T> List<T> getItemsByContainerId(Long containerId, JpaRepository<T, Long> repository) {
        // Логика для получения элементов по ID контейнера
        // Используйте репозиторий для поиска элементов
        // Пример:
        // return repository.findByContainerId(containerId);
        return new ArrayList<>();  // Временно, замените на реальную логику
    }

    public <T> void deleteAllItemsByContainerId(Long containerId, JpaRepository<T, Long> repository) {
        // Логика для удаления всех элементов по ID контейнера
        // Пример: repository.deleteByContainerId(containerId);
    }

    public <T> void updateQuantity(Long itemId, int quantity, JpaRepository<T, Long> repository) {
        // Логика для обновления количества элемента
        // Пример: найти элемент по ID и обновить его количество
    }

    public <T> boolean isProductInContainer(Long containerId, Long productId, JpaRepository<T, Long> repository) {
        // Логика для проверки, есть ли товар в контейнере
        // Пример: return repository.existsByContainerIdAndProductId(containerId, productId);
        return false;  // Временно, замените на реальную логику
    }
}
