package com.rca.stock.mapper;

import com.rca.stock.dto.stock.StockItemDto;
import com.rca.stock.model.StockItem;
import org.springframework.stereotype.Service;

@Service
public class StockMapper {

    public StockItem toStockItem(StockItemDto dto) {
        if (dto == null) {
            return null;
        }

        return StockItem.builder()
                .name(dto.name())
                .quantity(dto.quantity())
                .category(dto.category())
                .price(dto.price())
                .isNotified(false)
                .build();
    }
}
