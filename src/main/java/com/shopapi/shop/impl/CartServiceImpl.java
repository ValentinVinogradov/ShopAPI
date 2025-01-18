package com.shopapi.shop.impl;

import com.shopapi.shop.dto.CartItemResponseDTO;
import com.shopapi.shop.enums.CartTotalPriceOperation;
import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.models.Cart;
import com.shopapi.shop.models.Promocode;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repositories.CartRepository;
import com.shopapi.shop.repositories.UserRepository;
import com.shopapi.shop.services.CartService;
import com.shopapi.shop.utils.PriceUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final PromocodeServiceImpl promocodeService;


    public CartServiceImpl(CartRepository cartRepository,
                           UserRepository userRepository,
                           PromocodeServiceImpl promocodeService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.promocodeService = promocodeService;
    }

    @Override
    public List<CartItemResponseDTO> getCartItemsByUserId(long userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        return cart.getCartItems().stream()
                .map(cartItem -> new CartItemResponseDTO(cartItem.getId(),
                        cartItem.getId(),
                        cartItem.getProduct().getId(),
                        cartItem.getQuantity()))
                .toList();
    }

    @Transactional
    @Override
    public Cart createCart(Long userId) {
        // Проверяем, существует ли пользователь
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Создаем новую корзину
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);

        cartRepository.save(cart);
        return cart;// Сохраняем корзину в базе данных
    }

    @Transactional
    @Override
    public PromoCodeValidationStatus applyPromoCode(long cartId, String promoCode) {
        // Проверяем, существует ли корзина
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        System.out.println(cart);


        // Проверяем промокод
        PromoCodeValidationStatus status = promocodeService.validatePromocode(promoCode);

        if (status != PromoCodeValidationStatus.VALID) {
            return status;
        }

        Promocode promocode = promocodeService.getPromocodeByCode(promoCode);
        BigDecimal discountedCartTotalPrice = PriceUtils.calculateDiscountedPrice(
                cart.getTotalPrice(),
                promocode.getDiscountPercentage()
        );
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

    public boolean isCartExists(long userId) {
        return cartRepository.findByUser_Id(userId).isPresent();
    }

}
