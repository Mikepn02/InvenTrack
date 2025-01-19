package com.rca.stock.mapper;

import com.rca.stock.dto.order.OrderDto;
import com.rca.stock.dto.order.OrderResponseDto;
import com.rca.stock.model.Order;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    private OrderMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OrderMapper();
    }

    @Test
    public void shouldMapOrderDtoToOrder() {
        OrderDto dto = OrderDto.builder()
                .name("Increasing Rice Quantity")
                .supplierName("ABC Suppliers")
                .status("Pending")
                .date(LocalDate.now())
                .quantity(100)
                .stockItemName("Rice")
                .build();


        Order order = mapper.toOrder(dto);
        assertEquals(dto.getName(), order.getName());
        assertEquals(dto.getStatus(), order.getStatus());
        assertEquals(dto.getQuantity(), order.getQuantity());
        assertEquals(dto.getDate(), order.getDate());

    }

    @Test
    public  void should_throw_null_pointer_exception_when_orderDto_is_null(){
        var exp = assertThrows(NullPointerException.class, ()-> mapper.toOrder(null));
        assertEquals("The order dto should not be null" , exp.getMessage());
    }


    @Test
    public void shouldMapOrderToOrderDto() {
        Order order = Order.builder()
                .name("Increasing Rice Quantity")
                .status("Pending")
                .date(LocalDate.now())
                .quantity(100)
                .build();

        OrderResponseDto dto = mapper.toDto(order);

        assertEquals(order.getName(), dto.getName());
        assertEquals(order.getStatus(), dto.getStatus());
        assertEquals(order.getQuantity(), dto.getQuantity());
        assertEquals(order.getDate(), dto.getDate());

    }

}