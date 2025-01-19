package com.rca.stock.services.impl;

import com.rca.stock.model.Notification;
import com.rca.stock.repository.NotifcationRepository;
import com.rca.stock.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private  final NotifcationRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotification(Notification notification) {
        log.info("Sending WS notification to {} with payload {}",notification);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(notification.getUser().getId()),
                "/notification",
                notification
        );
    }

    @Override
    public Notification getNotification(Integer id) {
        Notification notification = repository.findById(id).orElse(null);
        return notification;
    }

    @Override
    public List<Notification> getAllNotifications() {
        List<Notification> notifications = repository.findAll();
        return notifications;
    }

    @Override
    public List<Notification> getUnreadNotifications() {
        return repository.findByIsRead(false);
    }

    @Override
    public List<Notification> getReadNotifications() {
        return repository.findByIsRead(true);
    }

    @Override
    public Notification markAsRead(Integer id) {
        Notification notification = repository.findById(id).orElse(null);
        if (notification != null && !notification.isRead()) {
            notification.setRead(true);
            repository.save(notification);
        }
        return notification;
    }
}
