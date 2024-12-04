package com.shopapi.shop.impl;

import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.models.Promocode;
import com.shopapi.shop.repository.PromocodeRepository;
import com.shopapi.shop.services.PromocodeService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PromocodeServiceImpl implements PromocodeService {
    private final PromocodeRepository promocodeRepository;

    @Override
    @Transactional
    public Promocode createPromocode(Promocode promocode) {
        return promocodeRepository.save(promocode);
    }

    @Override
    @Transactional
    public Promocode updatePromocode(Promocode promocode) {
        return null;
    }

    @Override
    public List<Promocode> getAllPromocodes() {
        return List.of();
    }

    @Override
    public Promocode getPromocodeById(Long id) {
        return promocodeRepository.findById(id).orElse(null);
    }

    @Override
    public Promocode getPromocodeByCode(String code) {
        return promocodeRepository.findByCode(code).orElse(null);
    }

    @Override
    @Transactional
    public void deletePromocode(Long id) {

    }

    @Override
    public PromoCodeValidationStatus validatePromocode(String code) {
        Optional<Promocode> optionalPromocode = promocodeRepository.findByCode(code);

        if (optionalPromocode.isEmpty()) {
            return PromoCodeValidationStatus.INVALID;
        }

        Promocode promocode = optionalPromocode.get();

        LocalDateTime nowTime = LocalDateTime.now();
        if (promocode.getEndDate() != null && nowTime.isAfter(promocode.getEndDate())) {
            return PromoCodeValidationStatus.EXPIRED;
        }

        return PromoCodeValidationStatus.VALID;
    }

}
