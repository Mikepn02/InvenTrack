package com.rca.stock.services.impl;

import com.rca.stock.dto.order.OrderDto;
import com.rca.stock.dto.order.OrderResponseDto;
import com.rca.stock.exception.OrderNotFoundException;
import com.rca.stock.mapper.OrderMapper;
import com.rca.stock.model.Order;
import com.rca.stock.model.StockItem;
import com.rca.stock.model.Supplier;
import com.rca.stock.repository.OrderRepository;
import com.rca.stock.repository.StockItemRepository;
import com.rca.stock.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private StockItemRepository stockItemRepository;

    @Mock
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void shouldCreateOrder() {
        OrderDto dto = OrderDto.builder()
                .name("Increasing Rice Quantity")
                .supplierName("ABC Suppliers")
                .status("Pending")
                .date(LocalDate.now())
                .quantity(100)
                .stockItemName("Rice")
                .build();

        Supplier supplier = new Supplier();
        supplier.setCompanyName("ABC Suppliers");

        StockItem stockItem = new StockItem();
        stockItem.setName("Rice");

        Order order = Order.builder()
                .name("Increasing Rice Quantity")
                .status("Pending")
                .date(LocalDate.now())
                .quantity(100)
                .build();

        when(supplierRepository.findByCompanyName(dto.getSupplierName())).thenReturn(supplier);
        when(stockItemRepository.findByName(dto.getStockItemName())).thenReturn(stockItem);
        when(orderMapper.toOrder(dto)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);

        Order orderCreated = orderService.createOrder(dto);


        assertNotNull(orderCreated);
        assertEquals(dto.getName(), orderCreated.getName());
        assertEquals(dto.getQuantity(), orderCreated.getQuantity());
        assertEquals(dto.getDate(), orderCreated.getDate());
        assertEquals(dto.getQuantity(), orderCreated.getQuantity());


        verify(orderRepository, times(1)).save(order);
    }


    @Test
    public void shouldReturnAllOrders() {
        List<Order> orders = new ArrayList<>();

        orders.add(Order.builder()
                .name("Increasing Rice Quantity")
                .status("Pending")
                .date(LocalDate.now())
                .quantity(100)
                .build()
        );

        when(orderRepository.findAll()).thenReturn(orders);

        OrderResponseDto mockOrderDTO =  OrderResponseDto.builder()
                .name("Increasing Rice Quantity")
                .status("Pending")
                .date(LocalDate.now())
                .quantity(100)
                .build();

        when(orderMapper.toDto(any(Order.class))).thenReturn(mockOrderDTO);

        List<Order> orderReportDtos = orderService.getAllOrders();
        assertEquals(orders.size(), orderReportDtos.size());

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void shouldFindOrderById() {

        Integer orderId = 1;

        Order order = Order.builder()
                .name("Increasing Rice Quantity")
                .status("Pending")
                .date(LocalDate.now())
                .quantity(100)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderResponseDto mockOrderDTO =  OrderResponseDto.builder()
                .name("Increasing Rice Quantity")
                .status("Pending")
                .date(LocalDate.now())
                .quantity(100)
                .build();

        when(orderMapper.toDto(any(Order.class))).thenReturn(mockOrderDTO);

        OrderResponseDto dto = orderService.getOrderById(orderId);

        assertEquals(dto.getName(), order.getName());
        assertEquals(dto.getQuantity(), order.getQuantity());
        assertEquals(dto.getDate(), order.getDate());
        assertEquals(dto.getQuantity(), order.getQuantity());

        verify(orderRepository, times(1)).findById(orderId);

    }


    @Test
    void updateOrder_ShouldUpdateOrder_WhenOrderExists() {
        Integer orderId = 1;

        Order existingOrder = Order.builder()
                .name("Old Order Name")
                .status("Old Status")
                .quantity(50)
                .date(LocalDate.now())
                .build();


        OrderDto orderDto =  OrderDto.builder()
                .name("New Order Name")
                .status("New Status")
                .supplierName("New Supplier")
                .stockItemName("New Stock Item")
                .quantity(100)
                .date(LocalDate.now())
                .build();


        Supplier  supplier = new Supplier();
        supplier.setCompanyName("New Supplier");

        StockItem stockItem = new StockItem();
        stockItem.setName("New Stock Item");


        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(supplierRepository.findByCompanyName(orderDto.getSupplierName())).thenReturn(supplier);
        when(stockItemRepository.findByName(orderDto.getStockItemName())).thenReturn(stockItem);
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        Order updatedOrder = orderService.updateOrder(orderId , orderDto);

        assertNotNull(updatedOrder);
        assertEquals(orderDto.getName() , updatedOrder.getName());
        assertEquals(orderDto.getQuantity(), updatedOrder.getQuantity());
        assertEquals(orderDto.getDate(), updatedOrder.getDate());
        assertEquals(orderDto.getStatus(), updatedOrder.getStatus());
        assertEquals(supplier , updatedOrder.getSupplier());
        assertEquals(stockItem , updatedOrder.getStockItem());


        verify(orderRepository, times(1)).findById(orderId);
        verify(supplierRepository, times(1)).findByCompanyName(orderDto.getSupplierName());
        verify(stockItemRepository, times(1)).findByName(orderDto.getStockItemName());
        verify(orderRepository, times(1)).save(existingOrder);
    }



    @Test
    void updateOrder_ShouldThrowException_WhenOrderDoesNotExist() {
        Integer orderId = 1;
        OrderDto orderDto =  OrderDto.builder()
                .name("New Order Name")
                .status("New Status")
                .supplierName("New Supplier")
                .stockItemName("New Stock Item")
                .quantity(100)
                .date(LocalDate.now())
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.updateOrder(orderId , orderDto));

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
    }


    @Test
    void deleteOrder_ShouldDeleteOrder_WhenOrderExists() {
        Integer orderId = 1;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.deleteOrder(orderId);

        verify(orderRepository).findById(orderId);
        verify(orderRepository).delete(order);
    }

    @Test
    void deleteOrder_ShouldThrowOrderNotFoundException_WhenOrderDoesNotExist() {
        Integer orderId = 1;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(orderId));

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).delete(any(Order.class));
    }

}