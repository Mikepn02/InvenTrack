package com.rca.stock.services;

import com.rca.stock.model.Notification;


import java.util.List;

public interface NotificationService {

    void sendNotification(Notification notification);
    Notification getNotification(Integer id);
    List<Notification> getAllNotifications();
    List<Notification> getUnreadNotifications();
    List<Notification> getReadNotifications();
    Notification markAsRead(Integer id);

}
