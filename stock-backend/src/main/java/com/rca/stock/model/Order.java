package com.rca.stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "_order")
public class Order {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private LocalDate date;
    private String status;

    @Column(name="quantity")
    private double quantity;

    @ManyToOne
    private StockItem stockItem;

    @ManyToOne
    private Supplier supplier;
}
