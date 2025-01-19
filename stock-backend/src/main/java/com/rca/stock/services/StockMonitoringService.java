package com.rca.stock.services;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


public interface StockMonitoringService {
    void monitorStockLevels();
}
