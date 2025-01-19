package com.rca.stock.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReportDto {
    private Integer orderId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private String supplierName;
}
