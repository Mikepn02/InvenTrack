package com.rca.stock.services;

import com.rca.stock.dto.stock.StockItemDto;
import com.rca.stock.model.StockItem;

import java.util.List;


public interface StockItemService {
    StockItem addStockItem(StockItemDto stockItemDto);

    StockItem updateStockItem(Integer id, StockItemDto stockItemDto);

    void deleteStockItem(Integer id);

    StockItem getStockItemById(Integer id);

    List<StockItem> getAllStockItems();

    List<StockItem> searchStockItems(String keyword);
    StockItem restockStockItem(Integer id , double additionalQuantity);
    List<StockItem> getTop5ConsumedStockItems();

//    void monitoringService();
    StockItem consumeStockItem(Integer id, double consumedAmount);


}
