package com.rca.stock.controller;

import com.rca.stock.dto.order.OrderDto;
import com.rca.stock.dto.order.OrderResponseDto;
import com.rca.stock.mapper.OrderMapper;
import com.rca.stock.model.Order;
import com.rca.stock.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderDto orderDto) {
        Order createdOrder = service.createOrder(orderDto);
        OrderResponseDto responseDto = orderMapper.toDto(createdOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<Order> orders = service.getAllOrders();
        List<OrderResponseDto> orderDtos = orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Integer orderId, @RequestParam String status) {
        Order updatedOrder = service.updateOrderStatus(orderId, status);
        OrderResponseDto responseDto = orderMapper.toDto(updatedOrder);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer orderId) {
        service.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
