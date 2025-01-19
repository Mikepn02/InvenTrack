package com.rca.stock.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderNotFoundException extends RuntimeException {
    private final String msg;
}
