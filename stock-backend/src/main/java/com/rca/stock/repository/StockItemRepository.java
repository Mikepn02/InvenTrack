package com.rca.stock.repository;

import com.rca.stock.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface StockItemRepository extends JpaRepository<StockItem, Integer> {
    StockItem findByName(String name);
    List<StockItem> findByCategoryContainingIgnoreCaseOrPriceEqualsOrQuantityEquals(String categoryKeyword, BigDecimal price, double quantity);
    List<StockItem> findByQuantityLessThan(double quantity);

    @Query("SELECT s FROM StockItem s WHERE s.quantity < 5")
    List<StockItem> findTop5ByConsumedQuantity();
}