package com.shopapi.shop.impl;

import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.models.Promocode;
import com.shopapi.shop.repository.PromocodeRepository;
import com.shopapi.shop.services.AbstractService;
import com.shopapi.shop.services.PromocodeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PromocodeServiceImpl extends AbstractService<Promocode, Long> implements PromocodeService {
    private final PromocodeRepository promocodeRepository;

    protected PromocodeServiceImpl(PromocodeRepository promocodeRepository) {
        super(promocodeRepository);
        this.promocodeRepository = promocodeRepository;
    }

    @Transactional
    @Override
    public void addPromocode(Promocode promocode) {
        promocodeRepository.save(promocode);
    }

    @Transactional
    @Override
    public void updatePromocode(Promocode promocode) {
        promocodeRepository.save(promocode);
    }

    @Override
    public Promocode getPromocodeByCode(String code) {
        return promocodeRepository.findByCode(code).orElse(null);
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
