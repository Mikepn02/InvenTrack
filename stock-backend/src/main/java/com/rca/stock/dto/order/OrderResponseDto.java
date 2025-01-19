package com.rca.stock.dto.order;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Data
public class OrderResponseDto {
    private Integer id;
    private String name;
    private String supplierName;
    private String status;
    private LocalDate date;
    private double quantity;
    private String stockItemName;
}

