package com.shopapi.shop.impl;

import com.shopapi.shop.dto.CartItemResponseDTO;
import com.shopapi.shop.dto.CartResponseDTO;
import com.shopapi.shop.enums.CartTotalPriceOperation;
import com.shopapi.shop.enums.PromoCodeValidationStatus;
import com.shopapi.shop.models.Cart;
import com.shopapi.shop.models.CartItem;
import com.shopapi.shop.models.Promocode;
import com.shopapi.shop.models.User;
import com.shopapi.shop.repositories.CartItemRepository;
import com.shopapi.shop.repositories.CartRepository;
import com.shopapi.shop.repositories.UserRepository;
import com.shopapi.shop.services.CartService;
import com.shopapi.shop.utils.PriceUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final PromocodeServiceImpl promocodeService;
    private final AddressServiceImpl addressService;



    public CartServiceImpl(CartRepository cartRepository,
                           UserRepository userRepository,
                           PromocodeServiceImpl promocodeService,
                           CartItemRepository cartItemRepository, AddressServiceImpl addressService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.promocodeService = promocodeService;
        this.cartItemRepository = cartItemRepository;
        this.addressService = addressService;
    }


    //todo респонс прееделать и добавить итоговую цену и кол-во всех товаров
    @Override
    public CartResponseDTO getCartById(UUID userId) {
        System.out.println("зашли в метод");
        Cart cart = getCartByUserId(userId);
        updateCartTotalPrice(cart);
        System.out.println("обновили цену");
        List<CartItemResponseDTO> cartItems = cart.getCartItems().stream()
                .map(cartItem -> new CartItemResponseDTO(
                        cartItem.getId(),
                        cart.getId(),
                        cartItem.getProduct().getId(),
                        cartItem.getProduct().getName(),
                        cartItem.getProduct().getDescription(),
                        cartItem.getProduct().getPrice(),
                        cartItem.getProduct().getImg().getFirst(),
                        cartItem.getQuantity()))
                .toList();
        System.out.println("промаппили");
        return new CartResponseDTO(
                addressService.getActiveAddedUserAddress(userId),
                cartItems,
                cart.getTotalPrice(),
                cart.getTotalCount()
        );
    }

    public Cart getCartByUserId(UUID userId) {
        return cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }

    //todo подумать над названием
    public void updateCartTotalPrice(Cart cart) {
        BigDecimal totalDiff = BigDecimal.valueOf(0);
        BigDecimal currentTotalPrice = cart.getTotalPrice();

        for (CartItem item : cart.getCartItems()) {
            BigDecimal productPrice = item.getProduct().getPrice(); // Текущая цена товара
            System.out.println("Цена товара: " + productPrice);
            BigDecimal itemLastPrice = item.getLastProductPrice(); // Цена товара на момент добавления
            System.out.println("Последняя цена cartItem: " + itemLastPrice);
            int itemQuantity = item.getQuantity();
            System.out.println("Кол-во товара: " + itemQuantity);
            if (productPrice.compareTo(itemLastPrice) != 0) {
                BigDecimal diff = productPrice.subtract(itemLastPrice).multiply(BigDecimal.valueOf(itemQuantity));
                System.out.println("Полученная разница: " + diff);
                totalDiff = totalDiff.add(diff); // Суммируем изменения

                item.setLastProductPrice(productPrice);
                cartItemRepository.save(item);
            }
        }

        cart.setTotalPrice(currentTotalPrice.add(totalDiff)); // Суммируем старую цену с изменениями
        cartRepository.save(cart);
    }


    @Transactional
    @Override
    public Cart createCart(UUID userId) {
        // Проверяем, существует ли пользователь
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Создаем новую корзину
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setTotalCount(1);

        cartRepository.save(cart);
        return cart;// Сохраняем корзину в базе данных
    }

    @Transactional
    @Override
    public PromoCodeValidationStatus applyPromoCode(UUID cartId, String promoCode) {
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

    public boolean isCartExists(UUID userId) {
        return cartRepository.findByUser_Id(userId).isPresent();
    }
}
