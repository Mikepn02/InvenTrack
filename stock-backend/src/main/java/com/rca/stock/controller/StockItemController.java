package com.rca.stock.controller;

import com.rca.stock.dto.stock.StockItemDto;
import com.rca.stock.model.StockItem;
import com.rca.stock.services.StockItemService;
import com.rca.stock.services.StockMonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("stock")
public class StockItemController {

    private final StockItemService service;
    private  final StockMonitoringService monitoringService;

    @PostMapping
    public ResponseEntity<StockItem> addStockItem(@RequestBody StockItemDto requestDto){
        var stock = service.addStockItem(requestDto);
        return ResponseEntity.ok(stock);
    }

    @PostMapping("/monitor")
    public ResponseEntity<String> monitorStock() {
        monitoringService.monitorStockLevels();
        return ResponseEntity.ok("Stock monitoring completed and notifications sent.");
    }



    @GetMapping
    public ResponseEntity<List<Map<String, StockItem>>> getAllStockItem() {
        List<StockItem> stockItems = service.getAllStockItems();

        List<Map<String, StockItem>> response = stockItems.stream()
                .map(stockItem -> Map.of("stock", stockItem))
                .toList();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<StockItem> getStockItemById(@PathVariable Integer id) {
        StockItem stockItem = service.getStockItemById(id);
        return ResponseEntity.ok(stockItem);
    }
    @PutMapping("/{id}")
    public ResponseEntity<StockItem> updateStockItem(@PathVariable Integer id, @RequestBody StockItemDto requestDto) {
        StockItem updatedStockItem = service.updateStockItem(id, requestDto);
        return ResponseEntity.ok(updatedStockItem);
    }

    @PostMapping("/{id}/consume")
    public ResponseEntity<String> consumeStockItem(@PathVariable Integer id, @RequestBody Map<String, Double> request) {
        double consumedAmount = request.get("consumedAmount");

        service.consumeStockItem(id, consumedAmount);

        return ResponseEntity.ok("Stock item consumed successfully.");
    }
    @PostMapping("/restock/{id}")
    public ResponseEntity<StockItem> restockStockItem(@PathVariable Integer id, @RequestBody Map<String, Double> request) {
        double additionalQuantity = request.get("additionalQuantity");
        StockItem restockedStockItem = service.restockStockItem(id, additionalQuantity);
        return ResponseEntity.ok(restockedStockItem);
    }

    @GetMapping("/top-consumed")
    public ResponseEntity<List<StockItem>> getTop5ConsumedStockItem() {
        List<StockItem> topStockItems = service.getTop5ConsumedStockItems();
        return ResponseEntity.ok(topStockItems);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockItem(@PathVariable Integer id) {
        service.deleteStockItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<StockItem>> searchStockItems(@RequestParam(name = "keyword") String keyword) {
        List<StockItem> stockItems = service.searchStockItems(keyword);
        return ResponseEntity.ok(stockItems);
    }

}
