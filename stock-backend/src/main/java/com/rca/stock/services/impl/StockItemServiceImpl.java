package com.rca.stock.services.impl;

import com.rca.stock.dto.notification.NotificationMessage;
import com.rca.stock.dto.stock.StockItemDto;
import com.rca.stock.exception.StockItemNotFoundException;
import com.rca.stock.mapper.NotificationMapper;
import com.rca.stock.mapper.StockMapper;
import com.rca.stock.model.User;
import com.rca.stock.repository.NotifcationRepository;
import com.rca.stock.model.StockItem;
import com.rca.stock.repository.StockItemRepository;
import com.rca.stock.services.NotificationService;
import com.rca.stock.services.StockItemService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockItemServiceImpl implements StockItemService {

    private final StockItemRepository repository;
    private final StockMapper mapper;
    private final NotificationService notificationService;
    private final NotifcationRepository notifcationRepository;
    private final NotificationMapper notificationMapper;


    @Override
    public StockItem addStockItem(StockItemDto stockItemDto) {
        return repository.save(mapper.toStockItem(stockItemDto));
    }

    @Override
    public StockItem updateStockItem(Integer id, StockItemDto stockItemDto) {
        var stockItem = repository.findById(id)
                .orElseThrow(() -> new StockItemNotFoundException(String.format("Can not update the stock item :: No Stock item with the id :: %s",id)));
        mergeStockItem(stockItem , stockItemDto);
        return repository.save(stockItem);
    }

    public void mergeStockItem(StockItem stockItem, StockItemDto dto) {
        if (StringUtils.isNotBlank(dto.category())) {
            stockItem.setCategory(dto.category());
        }

        if (dto.quantity() != 0.0) {
            stockItem.setQuantity(dto.quantity());
        }

    }


    @Override
    public void deleteStockItem(Integer id) {
        var stockItem = repository.findById(id)
                .orElseThrow(() -> new StockItemNotFoundException("Stock item with that id not found"));

        repository.delete(stockItem);
    }

    @Override
    public StockItem getStockItemById(Integer id) {
        var stockItem = repository.findById(id)
                .orElseThrow(() -> new StockItemNotFoundException("Stock item with that id not found"));
        return stockItem;
    }

    @Override
    public List<StockItem> getAllStockItems() {
        return repository.findAll();
    }

    @Override
    public List<StockItem> searchStockItems(String keyword) {
        BigDecimal price = parseBigDecimal(keyword);
        double quantity = parseDouble(keyword);

        return repository.findByCategoryContainingIgnoreCaseOrPriceEqualsOrQuantityEquals(keyword, price, quantity);
    }

    @Override
    public StockItem restockStockItem(Integer id,double additionalQuantity) {
        StockItem stockItem = getStockItemById(id);
        stockItem.setQuantity(stockItem.getQuantity() + additionalQuantity);
        stockItem.setConsumedQuantity(Math.max(stockItem.getConsumedQuantity() - additionalQuantity, 0));
        stockItem.setIsNotified(false);
        return repository.save(stockItem);
    }

    @Override
    public List<StockItem> getTop5ConsumedStockItems() {
        List<StockItem> topConsumedItems = repository.findTop5ByConsumedQuantity();
        return topConsumedItems;
    }

    @Override
    public StockItem consumeStockItem(Integer id, double consumeAmount) {
        StockItem stockItem = getStockItemById(id);
        if (stockItem.getQuantity() < consumeAmount) {
            throw new IllegalArgumentException("Not enough stock available.");
        }

        stockItem.setQuantity(stockItem.getQuantity() - consumeAmount);
        stockItem.setConsumedQuantity(stockItem.getConsumedQuantity() + consumeAmount);
        checkAndNotifyLowStock(stockItem);

        repository.save(stockItem);
        return stockItem;
    }


    private void checkAndNotifyLowStock(StockItem item) {

        Authentication connectedUser = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) connectedUser.getPrincipal();
        System.out.println("Here is the user found: "+ user.fullName());
        if (item.getQuantity() < 10 && !item.getIsNotified()) {
            var notificationMessage = new NotificationMessage(
                    item.getName(),
                    item.getCategory(),
                    "Stock level is low for category: " + item.getCategory(),
                    item.getQuantity(),
                    user,
                    user.getEmail()
            );

            var notification = notificationMapper.toNotification(notificationMessage);
            notifcationRepository.save(notification);
            notificationService.sendNotification(notification);

            item.setIsNotified(true);
        }
    }



    private BigDecimal parseBigDecimal(String str) {
        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
