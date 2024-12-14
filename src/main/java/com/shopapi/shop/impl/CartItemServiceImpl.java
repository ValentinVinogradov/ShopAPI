package com.shopapi.shop.impl;

import com.shopapi.shop.dto.CartItemRequestDTO;
import com.shopapi.shop.enums.CartTotalPriceOperation;
import com.shopapi.shop.models.Cart;
import com.shopapi.shop.models.CartItem;
import com.shopapi.shop.models.Product;
import com.shopapi.shop.repository.CartItemRepository;
import com.shopapi.shop.repository.CartRepository;
import com.shopapi.shop.repository.ProductRepository;
import com.shopapi.shop.services.CartItemService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {
    private static final int DEFAULT_ITEM_VALUE = 1;
    private static final CartTotalPriceOperation add = CartTotalPriceOperation.ADD;
    private static final CartTotalPriceOperation subtract = CartTotalPriceOperation.SUBTRACT;

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartServiceImpl cartService;


    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               CartRepository cartRepository,
                               ProductRepository productRepository,
                               CartServiceImpl cartService) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    @Override
    public CartItem getCartItemById(long id) {
        return cartItemRepository.findById(id).orElse(null);
    }

    public List<CartItem> getItemsByCartId(Long cartId) {
        return cartItemRepository.getAllByCartId(cartId);
    }

    @Transactional
    public void deleteAllItemsByCartId(Long cartId) {
        cartItemRepository.deleteAllByCartId(cartId);
    }

    @Transactional
    private Cart setCart(Long userId, Long cartId) {
        Cart cart;
        if (cartId != null) {
            // Если cartId передан, ищем корзину по cartId
            cart = cartRepository.findById(cartId).orElse(null);
        } else {
//          Если cartId не передан, ищем корзину по userId
            cart = cartRepository.findByUserId(userId);
        }

        if (cart == null) {
            cartService.createCart(userId);
            cart = cartRepository.findByUserId(userId);

        }

        return cart;
    }

    @Transactional
    @Override
    public void addCartItem(CartItemRequestDTO cartItemRequestDTO) {

        Long userId = cartItemRequestDTO.getUserId();
        Long cartId = cartItemRequestDTO.getCartId();
        Long productId = cartItemRequestDTO.getProductId();

        Cart cart = setCart(userId, cartId);

        // Проверяем, существует ли уже товар в корзине
        Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        BigDecimal productPrice = product.getPrice();
        CartItem existingCartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (existingCartItem != null) {
            // Если товар уже есть в корзине, увеличиваем количество
            updateQuantity(existingCartItem, 1);
            cartItemRepository.save(existingCartItem); // Сохраняем изменения
        } else {
            // Если товара в корзине нет, создаем новый CartItem
            CartItem cartItem = new CartItem(cart, product, DEFAULT_ITEM_VALUE);
            // Сохраняем новый CartItem в базе данных
            cartItemRepository.save(cartItem);
        }
        cartService.updateTotalPrice(cart, productPrice, add);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void deleteCartItem(CartItemRequestDTO cartItemRequestDTO) {
        Long cartId = cartItemRequestDTO.getCartId();
        Long productId = cartItemRequestDTO.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        BigDecimal productPrice = product.getPrice();
        Cart cart;
        CartItem existingCartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);
        if (existingCartItem != null) {
            if (existingCartItem.getQuantity() > 1) {
                updateQuantity(existingCartItem, -1);
                cartItemRepository.save(existingCartItem);
            } else {
                cartItemRepository.deleteById(existingCartItem.getId());
            }

            cart = cartRepository.findById(cartId).orElse(null);
            System.out.println(cart);
            if (cart != null) {
                cartService.updateTotalPrice(cart, productPrice, subtract);
                cartRepository.save(cart);
            }
        }
    }

    @Override
    public void updateQuantity(CartItem cartItem, int quantityChange) {
        int currentQuantity = cartItem.getQuantity();
        cartItem.setQuantity(currentQuantity + quantityChange);
    }

    //todo
    @Transactional
    public void updateCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }


}