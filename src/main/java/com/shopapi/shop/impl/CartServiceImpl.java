package com.shopapi.shop.impl;

import com.shopapi.shop.enums.CartTotalPriceOperation;
import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.models.Cart;
import com.shopapi.shop.models.Promocode;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repository.CartRepository;
import com.shopapi.shop.repository.PromocodeRepository;
import com.shopapi.shop.repository.UserRepository;
import com.shopapi.shop.services.AbstractService;
import com.shopapi.shop.services.CartService;
import com.shopapi.shop.services.PromocodeService;
import com.shopapi.shop.utils.PriceUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


@Service
public class CartServiceImpl extends AbstractService<Cart, Long> implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final PromocodeServiceImpl promocodeService;

    public CartServiceImpl(JpaRepository<Cart, Long> repository,
                           CartRepository cartRepository,
                           UserRepository userRepository,
                           PromocodeServiceImpl promocodeService) {
        super(repository);
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.promocodeService = promocodeService;
    }

    @Transactional
    @Override
    public void createCart(Long userId) {
        // Проверяем, существует ли пользователь
        User user = userRepository.findById(userId)
                .orElse(null);

        // Создаем новую корзину
        Cart cart = new Cart();
        if (user != null) {
            cart.setUser(user);
        }
        cart.setTotalPrice(BigDecimal.ZERO);

        // Сохраняем корзину в базе данных
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public PromoCodeValidationStatus applyPromoCode(long cartId, String promoCode) {
        // Проверяем, существует ли корзина
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        System.out.println(cart);


        // Проверяем промокод
        PromoCodeValidationStatus status = promocodeService.validatePromocode(promoCode);

        if (status != PromoCodeValidationStatus.VALID) {
            return status;
        }

        Promocode promocode = promocodeService.getPromocodeByCode(promoCode);
        BigDecimal discountedCartTotalPrice = PriceUtils.calculateDiscountedPrice(cart.getTotalPrice(), promocode.getDiscountPercentage());
        //todo какая то логика на сохранение того что промокод был активирован
        cart.setTotalPrice(discountedCartTotalPrice);
        cartRepository.save(cart);
        return PromoCodeValidationStatus.VALID;
    }

    @Transactional
    @Override
    public void updateTotalPrice(Cart cart, BigDecimal price, CartTotalPriceOperation operation) {
        if (operation == CartTotalPriceOperation.ADD) {
            BigDecimal newTotalPrice = cart.getTotalPrice().add(price);
            cart.setTotalPrice(newTotalPrice);
        } else if (operation == CartTotalPriceOperation.SUBTRACT) {

            BigDecimal newTotalPrice = cart.getTotalPrice().subtract(price);
            cart.setTotalPrice(newTotalPrice);
        }
    }

}
