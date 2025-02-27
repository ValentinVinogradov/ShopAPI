package com.shopapi.shop.impl;

import com.shopapi.shop.dto.*;
import com.shopapi.shop.enums.OrderStatus;
import com.shopapi.shop.models.*;
import com.shopapi.shop.repositories.CartRepository;
import com.shopapi.shop.repositories.OrderItemRepository;
import com.shopapi.shop.repositories.OrderRepository;
import com.shopapi.shop.utils.TrackingNumberGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class OrderServiceImpl {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserServiceImpl userService;
    private final CartServiceImpl cartService;
    private final CartItemServiceImpl cartItemService;
    private final AddressServiceImpl addressService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            OrderItemRepository orderItemRepository,
                            UserServiceImpl userService,
                            CartServiceImpl cartService,
                            CartItemServiceImpl cartItemService,
                            AddressServiceImpl addressService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.addressService = addressService;
    }

    //todo где-то тут проверка

    //todo !!!! подумать что такое паттерн билдер чтобы методами быстро сделать объект
    @Transactional
    public void createOrder(UUID userId, OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        User user = userService.getById(userId);
        Cart cart = cartService.getCartByUserId(userId);
        List<Long> cartItemIds = orderRequestDTO.cartItemIds();
        List<CartItem> selectedCartItems = cartItemService.getSelectedCartItems(cart.getId(), cartItemIds);

        List<OrderItem> orderItems = selectedCartItems.stream()
                .map((cartItem) -> createOrderItemFromCartItem(cartItem, order))
                .toList();

        BigDecimal subTotalPrice = cartItemService.calculateSubTotalPrice(selectedCartItems);
        Integer subTotalCount = cartItemService.calculateSubTotalCount(selectedCartItems);


        // Добавляем созданные OrderItem в заказ
        order.setOrderItems(orderItems);
        order.setCreatedAt(LocalDate.now());
        order.setDeliveryDate(LocalDate.now().plusDays(14));
        order.setShippingAddress(addressService.getActiveAddedUserAddress(userId));
        order.setTotalPrice(subTotalPrice); //todo сделать пересчет как-то (динамический подсчет)
        order.setTotalCount(subTotalCount);
        order.setOrderStatus(OrderStatus.NEW);
        order.setOrderPaymentStatus(orderRequestDTO.orderPaymentStatus());
        order.setTrackingNumber(TrackingNumberGenerator.generateTrackingNumber());
        order.setUser(user);

        cartItemService.deleteSelectedCartItems(userId, cartItemIds);
        cartRepository.save(cart);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
    }

    //todo подумать как потом это оптимизировать
    public PreviewResponseDTO showOrderPreview(UUID userId, PreviewRequestDTO previewRequestDTO) {
        Cart cart = cartService.getCartByUserId(userId);
        List<Long> cartItemIds = previewRequestDTO.cartItemIds();
        List<CartItem> selectedCartItems = cartItemService.getSelectedCartItems(cart.getId(), cartItemIds);
        List<CartItemResponseDTO> mappedCartItems = selectedCartItems
                .stream()
                .filter((cartItem) -> cartItemIds.contains(cartItem.getId()))
                .map((cartItem) -> {
                    Product product = cartItem.getProduct();
                    return new CartItemResponseDTO(
                            cartItem.getId(),
                            cartItem.getCart().getId(),
                            product.getId(),
                            product.getName(),
                            product.getDescription(),
                            cartItem.getLastProductPrice(),
                            product.getImg().getFirst(),
                            cartItem.getQuantity());
                })
                .toList();


        BigDecimal subTotalPrice = cartItemService.calculateSubTotalPrice(selectedCartItems);

        //todo добавить расчет стоимостей
        BigDecimal discountPrice = null;
        BigDecimal promocodePrice = null;
        BigDecimal certificatePrice = null;
        String address = addressService.getActiveAddedUserAddress(userId);
        Integer totalCount = cart.getTotalCount();

        return new PreviewResponseDTO(
                address,
                totalCount,
                subTotalPrice,
                discountPrice,
                promocodePrice,
                certificatePrice,
                mappedCartItems);
    }

    //todo сделать первую фотку как в карт айтемах в DTO
    private OrderItem createOrderItemFromCartItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setProductPrice(cartItem.getLastProductPrice());
        orderItem.setOrder(order);
        return orderItem;
    }

    //todo сделать метод для превью и в основном методе сделать пересчет итоговой стоимости все равно для проверки
    // сделать еще применение промокода у меня или применение сертификата



    public List<OrderResponseDTO> getOrdersByUserId(UUID userId) {
        List<Order> userOrders = orderRepository.findAllByUser_Id(userId);
        return userOrders
                .stream()
                .map(this::mapOrderToDTO)
                .toList();
    }

    public OrderResponseDTO getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return mapOrderToDTO(order);
    }

    private OrderResponseDTO mapOrderToDTO(Order order) {
        return new OrderResponseDTO(
                order.getOrderItems().stream()
                        .map(orderItem -> new OrderItemResponseDTO(
                                orderItem.getProduct().getId(),
                                orderItem.getQuantity(),
                                orderItem.getProductPrice()
                        ))
                        .toList(),
                order.getOrderStatus(),
                order.getTotalPrice(),
                order.getTotalCount(),
                order.getShippingAddress(),
                order.getShippingCost(),
                order.getDeliveryDate(),
                order.getTrackingNumber()
        );
    }

    public List<Order> getOrdersByStatus(String status) {
        return List.of();
    }

    //todo добавить сюда методы подсчета цен итоговых
    public BigDecimal calculateTotalPrice(UUID orderId) {
        return null;
    }
}
