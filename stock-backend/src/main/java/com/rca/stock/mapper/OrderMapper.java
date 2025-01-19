package com.rca.stock.mapper;

import com.rca.stock.dto.order.OrderDto;
import com.rca.stock.dto.order.OrderResponseDto;
import com.rca.stock.model.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {

    public Order toOrder(OrderDto dto) {
        if (dto == null) {
            throw new NullPointerException("The order dto should not be null");
        }

        return Order.builder()
                .name(dto.getName())
                .date(dto.getDate())
                .quantity(dto.getQuantity())
                .status(dto.getStatus())
                .build();
    }
    public OrderResponseDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        return OrderResponseDto.builder()
                .id(order.getId())
                .name(order.getName())
                .supplierName(order.getSupplier() != null ? order.getSupplier().getCompanyName() : null)
                .status(order.getStatus())
                .date(order.getDate())
                .quantity(order.getQuantity())
                .stockItemName(order.getStockItem() != null ? order.getStockItem().getName() : null)
                .build();
    }
}
