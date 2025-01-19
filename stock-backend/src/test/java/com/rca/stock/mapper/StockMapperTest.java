package com.rca.stock.mapper;

import com.rca.stock.dto.stock.StockItemDto;
import com.rca.stock.model.StockItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class StockMapperTest {

    private StockMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new StockMapper();
    }


    @Test
    public void should_mapStockItemDto_to_StockItem(){
        StockItemDto stockItemDto = new StockItemDto(
                "Rice",
                "food",
                100,
                BigDecimal.valueOf(100000.0),
                false

        );


        StockItem stockItem = mapper.toStockItem(stockItemDto);

        assertNotNull(stockItem);
        assertEquals(stockItemDto.name(), stockItem.getName());
        assertEquals(stockItemDto.price(), stockItem.getPrice());
        assertEquals(stockItemDto.quantity(), stockItem.getQuantity());
        assertEquals(stockItemDto.isNotified(), stockItem.getIsNotified());
        assertEquals(stockItemDto.category(), stockItem.getCategory());
    }
}