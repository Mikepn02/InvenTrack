package com.rca.stock.services;

import com.rca.stock.dto.order.OrderDto;
import com.rca.stock.model.Order;


import java.util.List;


public interface OrderService {

    Order createOrder(OrderDto orderDto);

    List<Order> getAllOrders();

    Order updateOrderStatus(Integer orderId, String status);

    Order updateOrder(Integer orderId, OrderDto orderDto);

    void deleteOrder(Integer orderId);
}
