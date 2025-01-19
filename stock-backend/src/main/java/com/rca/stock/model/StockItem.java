package com.rca.stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "_stock_item")
public class StockItem {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;
    private String category;
    private double quantity;
    private BigDecimal price;

    @Column(name = "is_notified")
    private Boolean isNotified=false;

    @Column(name = "consumed_quantity")
    private double consumedQuantity;

}
