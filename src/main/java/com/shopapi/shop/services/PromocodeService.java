package com.shopapi.shop.services;

import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.models.Promocode;

import java.util.List;

public interface PromocodeService {
    void addPromocode(Promocode promocode); // Создать
    void updatePromocode(Promocode promocode); // Обновить
    Promocode getPromocodeByCode(String code); // Поиск по коду
    PromoCodeValidationStatus validatePromocode(String code); // Проверить валидность
}
