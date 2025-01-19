package com.rca.stock.dto.stock;


import java.math.BigDecimal;

public record StockItemDto(
        String name,
        String category,
        double quantity,
        BigDecimal price,
        boolean isNotified
) {}
