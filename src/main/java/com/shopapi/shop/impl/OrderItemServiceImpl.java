package com.shopapi.shop.impl;

import com.shopapi.shop.models.OrderItem;
import com.shopapi.shop.repository.OrderItemRepository;
import com.shopapi.shop.services.*;
import org.springframework.stereotype.Service;

import java.util.List;

/*

 */
@Service
public class OrderItemServiceImpl extends AbstractService<OrderItem, Long> implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ItemAddiction itemAddiction;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ItemAddiction itemAddiction) {
        super(orderItemRepository);
        this.orderItemRepository = orderItemRepository;
        this.itemAddiction = itemAddiction;
    }

    @Override
    public List<OrderItem> getItemsByContainerId(Long containerId) {
        return itemAddiction.getItemsByContainerId(containerId, orderItemRepository);
    }

    @Override
    public void deleteAllItemsByContainerId(Long containerId) {
        itemAddiction.deleteAllItemsByContainerId(containerId, orderItemRepository);
    }

    @Override
    public void updateQuantity(Long itemId, int quantity) {
        itemAddiction.updateQuantity(itemId, quantity, orderItemRepository);
    }

    @Override
    public boolean isProductInContainer(Long containerId, long productId) {
        return itemAddiction.isProductInContainer(containerId, productId, orderItemRepository);
    }
}
