package com.rca.stock.services.impl;

import com.rca.stock.dto.order.OrderDto;
import com.rca.stock.dto.order.OrderResponseDto;
import com.rca.stock.exception.OrderNotFoundException;
import com.rca.stock.mapper.OrderMapper;
import com.rca.stock.model.Order;
import com.rca.stock.model.Supplier;
import com.rca.stock.model.StockItem;
import com.rca.stock.repository.OrderRepository;
import com.rca.stock.repository.SupplierRepository;
import com.rca.stock.repository.StockItemRepository;
import com.rca.stock.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final SupplierRepository supplierRepository;
    private final StockItemRepository stockItemRepository;
    private final OrderMapper mapper;

    @Override
    public Order createOrder(OrderDto orderDto) {
        Supplier supplier = supplierRepository.findByCompanyName(orderDto.getSupplierName());
        StockItem stockItem = stockItemRepository.findByName(orderDto.getStockItemName());

        if (supplier == null || stockItem == null) {
            throw new OrderNotFoundException("Supplier or StockItem not found");
        }
        Order order = mapper.toOrder(orderDto);
        order.setSupplier(supplier);
        order.setStockItem(stockItem);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    public OrderResponseDto getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("No Order found with ID: %s", orderId)));
        return mapper.toDto(order);
    }

    @Override
    public Order updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("No Order found with ID: %s", orderId)));

        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Integer orderId, OrderDto orderDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("No Order found with ID: %s", orderId)));

        mergeOrder(order , orderDto);

        return orderRepository.save(order);
    }

    public void mergeOrder(Order order, OrderDto orderDto) {
        if (StringUtils.isNotBlank(orderDto.getStatus())) {
            order.setStatus(orderDto.getStatus());
        }
        if (StringUtils.isNotBlank(orderDto.getName())) {
            order.setName(orderDto.getName());
        }
        if (StringUtils.isNotBlank(orderDto.getSupplierName())) {
            Supplier supplier = supplierRepository.findByCompanyName(orderDto.getSupplierName());
            order.setSupplier(supplier);
        }
        if (StringUtils.isNotBlank(orderDto.getStockItemName())) {
            StockItem stockItem = stockItemRepository.findByName(orderDto.getStockItemName());
            order.setStockItem(stockItem);
        }

        if(orderDto.getQuantity() != 0.0){
            order.setQuantity(orderDto.getQuantity());
        }
    }

    @Override
    public void deleteOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(String.format("No Order found with ID: %s", orderId)));

        orderRepository.delete(order);
    }
}
