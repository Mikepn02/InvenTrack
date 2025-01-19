package com.rca.stock.exception;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StockItemNotFoundException extends RuntimeException {
    private final String msg;
}
