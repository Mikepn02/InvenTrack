package com.rca.stock.services.impl;

import com.rca.stock.dto.notification.NotificationMessage;
import com.rca.stock.mapper.NotificationMapper;
import com.rca.stock.model.StockItem;
import com.rca.stock.model.User;
import com.rca.stock.repository.NotifcationRepository;
import com.rca.stock.repository.StockItemRepository;
import com.rca.stock.repository.UserRepository;
import com.rca.stock.services.NotificationService;
import com.rca.stock.services.StockMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockMonitoringServiceImpl implements StockMonitoringService {

    private final StockItemRepository repository;
    private final NotificationMapper mapper;
    private final NotifcationRepository notifcationRepository;
    private final NotificationService service;
    private final UserRepository userRepository;

    @Override
    public void monitorStockLevels() {
        List<StockItem> lowStockItems = repository.findByQuantityLessThan(10);
        log.info("Found {} items with low stock", lowStockItems.size());
        lowStockItems.forEach(this::notifyLowStock);
    }

    private void notifyLowStock(StockItem item) {
        if (item.getIsNotified()) {
            log.info("Notification already sent for item: {}", item.getName());
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        }
        if (user == null) {
            log.warn("No authenticated user found, cannot send notification.");
            return;
        }

        var notificationMessage = new NotificationMessage(
                item.getName(),
                item.getCategory(),
                "Stock level is low for category: " + item.getCategory(),
                item.getQuantity(),
                user,
                user.getEmail()
        );
        var notification = mapper.toNotification(notificationMessage);

        notification.setUser(user);

        notifcationRepository.save(notification);
        service.sendNotification(notification);

        item.setIsNotified(true);
        repository.save(item);
    }


}
