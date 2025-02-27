package com.shopapi.shop.impl;

import com.shopapi.shop.enums.CartTotalPriceOperation;
import com.shopapi.shop.models.Cart;
import com.shopapi.shop.models.CartItem;
import com.shopapi.shop.models.Product;
import com.shopapi.shop.repositories.CartItemRepository;
import com.shopapi.shop.repositories.CartRepository;
import com.shopapi.shop.repositories.ProductRepository;
import com.shopapi.shop.services.CartItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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

//    @Override
//    public CartItemResponseDTO getCartItemById(long cartItemId) {
//        CartItem cartItem = cartItemRepository.findById(cartItemId)
//                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
//        Product product = cartItem.getProduct();
//        return new CartItemResponseDTO(
//                cartItem.getId(),
//                cartItem.getCart().getId(),
//                product.getId(),
//                product.getName(),
//                product.getDescription(),
//                product.getPrice(),
//                product.getImg().getFirst(),
//                cartItem.getQuantity());
//    }

//    public List<CartItem> getItemsByCartId(Long cartId) {
//        return cartItemRepository.getAllByCartId(cartId);
//    }

//    @Transactional
//    public void deleteAllItemsByCartId(Long cartId) {
//        cartItemRepository.deleteAllByCart_Id(cartId);
//    }


    public List<CartItem> getSelectedCartItems(UUID cartId, List<Long> cartItemIds) {
        return cartItemRepository.findAllByCart_IdAndIdIn(cartId, cartItemIds);
    }


    //todo! подумать над тем нужен ли вообще userId в плане того чтобы создать корзину изначально либо после 1го
    // добавления товара в корзину (вроде как нужен)
    @Transactional
    private Cart setCart(UUID userId) {
        Cart cart = cartRepository.findByUser_Id(userId).orElse(null);
        if (cart == null) {
            return cartService.createCart(userId);
        } else {
            cart.setTotalCount(cart.getTotalCount() + 1);
            return cart;
        }

    }

    @Transactional
    public void addNewItem(UUID userId, UUID productId) {
        Cart cart = setCart(userId);
        // Проверяем, существует ли уже товар в корзине
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        BigDecimal productPrice = product.getPrice();

        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setQuantity(DEFAULT_ITEM_VALUE);
        newItem.setProduct(product);
        newItem.setLastProductPrice(productPrice);


        cartService.updateTotalPrice(cart, productPrice, add);
        newItem.setLastProductPrice(productPrice);
        //todo подумать над тем что будет если поменяется цена в
        // моменте добавления товара через "+"
        cartItemRepository.save(newItem);
        cartRepository.save(cart);
    }


    @Transactional
    public void increaseQuantity(UUID userId, Long cartItemId) {
        Cart cart = cartService.getCartByUserId(userId);
        cart.setTotalCount(cart.getTotalCount() + 1);
//        CartItem cartItem = cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), productId)
//                .orElseThrow(() -> new EntityNotFoundException("Cart Item not found"));
        CartItem cartItem = cartItemRepository.findByIdAndCart_Id(cartItemId, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart Item not found"));
        updateQuantity(cartItem, 1);
        cartService.updateTotalPrice(cart, cartItem.getProduct().getPrice(), add);
//        newItem.setLastProductPrice(productPrice); //todo подумать над тем что делать с ценой
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Transactional
    public void decreaseQuantity(UUID userId, Long cartItemId) {
        Cart cart = cartService.getCartByUserId(userId);
        cart.setTotalCount(cart.getTotalCount() - 1);

//        CartItem cartItem = cartItemRepository.findByCart_IdAndProduct_Id(cart.getId(), productId)
//                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
        CartItem cartItem = cartItemRepository.findByIdAndCart_Id(cartItemId, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart Item not found"));
        if (cartItem.getQuantity() <= 1) {
                throw new IllegalStateException("Cannot reduce quantity below 1. Use full item removal instead.");
        }
        updateQuantity(cartItem, -1);
        cartService.updateTotalPrice(cart, cartItem.getProduct().getPrice(), subtract);
        cartRepository.save(cart);
    }

    @Transactional
    public void deleteCartItemById(UUID userId, Long cartItemId) {
        Cart cart = cartService.getCartByUserId(userId);

        CartItem cartItem = cartItemRepository.findByIdAndCart_Id(cartItemId, cart.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Cart Item not found"));

        cart.setTotalPrice(cart.getTotalPrice()
                .subtract(cartItem.getLastProductPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()))));

        cart.setTotalCount(cart.getTotalCount() - cartItem.getQuantity());

        cartItemRepository.deleteByIdAndCart_Id(cartItemId, cart.getId());

        // Сохраняем изменения
        cartRepository.save(cart);  // Это сохранит изменения в корзине и удалит orphan
    }

    //todo надо менять итоговую цену!!!
    @Transactional
    public void deleteSelectedCartItems(UUID userId, List<Long> cartItemIds) {
        Cart cart = cartService.getCartByUserId(userId);


        //todo можно и без хранения в памяти цену считать а на уровне метода репозитория но там трудный запрос
        List<CartItem> selectedCartItems = cartItemRepository.findAllByCart_IdAndIdIn(cart.getId(), cartItemIds);


        cartItemRepository.deleteAllByCart_IdAndIdIn(cart.getId(), cartItemIds); //todo вроде как так можно


        BigDecimal subTotalPrice = calculateSubTotalPrice(selectedCartItems);
        Integer subTotalCount = calculateSubTotalCount(selectedCartItems);

        cart.setTotalPrice(cart.getTotalPrice().subtract(subTotalPrice));
        cart.setTotalCount(cart.getTotalCount() - subTotalCount);
        cartRepository.save(cart);
    }

    public BigDecimal calculateSubTotalPrice(List<CartItem> selectedCartItems) {
        return selectedCartItems
                .stream()
                .map(cartItem -> cartItem.getLastProductPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer calculateSubTotalCount(List<CartItem> selectedCartItems) {
        return selectedCartItems
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
//    @Transactional
//    public void deleteSelectedCartItems(UUID userId, List<UUID> productIds) {
//        Cart cart = cartRepository.findByUser_Id(userId)
//                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
//
//        cartItemRepository.deleteAllByCart_IdAndProduct_IdIn(cart.getId(), productIds);
//
//
//        cartRepository.save(cart);
//    }

//    @Transactional
//    @Override
//    public void addCartItem(UUID userId, UUID productId) {
//        Cart cart = setCart(userId);
//
//        // Проверяем, существует ли уже товар в корзине
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
//        BigDecimal productPrice = product.getPrice();
//
//        CartItem newItem;
//
//        Optional<CartItem> existingCartItem = cartItemRepository
//                .findByCart_IdAndProduct_Id(cart.getId(), product.getId());
//
//        if (existingCartItem.isPresent()) {
//            newItem = existingCartItem.get();
//            updateQuantity(newItem, 1);
//        } else {
//            newItem = new CartItem();
//            newItem.setCart(cart);
//            newItem.setQuantity(DEFAULT_ITEM_VALUE);
//            newItem.setProduct(product);
//            newItem.setLastProductPrice(productPrice);
//        }
//
//        cartService.updateTotalPrice(cart, productPrice, add);
//        newItem.setLastProductPrice(productPrice);
//        //todo подумать над тем что будет если поменяется цена в
//        // моменте добавления товара через "+"
//        cartItemRepository.save(newItem);
//        cartRepository.save(cart);
//    }
//
//    @Transactional
////    @Override
//    //todo это лишь кнопка "-" как на вб
//    public void deleteCartItem(UUID userId, UUID productId) {
//        Cart cart = cartRepository.findByUser_Id(userId)
//                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
//
//        Optional<CartItem> existingCartItem = cartItemRepository
//                .findByCart_IdAndProduct_Id(cart.getId(), product.getId());
//
//        if (existingCartItem.isPresent()) {
//            CartItem cartItem = existingCartItem.get();
//            if (existingCartItem.get().getQuantity() <= 1) {
//                throw new IllegalStateException("Cannot reduce quantity below 1. Use full item removal instead.");
//            }
//            updateQuantity(cartItem, -1);
//            cartService.updateTotalPrice(cart, cartItem.getProduct().getPrice(), subtract);
//            cartRepository.save(cart);
//        } else {
//            throw new EntityNotFoundException("Cart item not found");
//        }
//    }

    @Override
    public void updateQuantity(@NotNull CartItem cartItem, int quantityChange) {
        int currentQuantity = cartItem.getQuantity();
        cartItem.setQuantity(currentQuantity + quantityChange);
    }

    @Transactional
    public void updateCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }
}