package com.shopapi.shop.services;

import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.models.Promocode;

import java.util.List;

public interface PromocodeService {
    Promocode createPromocode(Promocode promocode); // Создать
    Promocode updatePromocode(Promocode promocode); // Обновить
    List<Promocode> getAllPromocodes(); // Получить все
    Promocode getPromocodeById(Long id); // Поиск по ID
    Promocode getPromocodeByCode(String code); // Поиск по коду
    void deletePromocode(Long id); // Удалить
    PromoCodeValidationStatus validatePromocode(String code); // Проверить валидность
}
