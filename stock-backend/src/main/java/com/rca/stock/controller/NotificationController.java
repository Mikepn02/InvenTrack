package com.rca.stock.controller;

import com.rca.stock.model.Notification;
import com.rca.stock.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("notify")
@RestController
public class NotificationController {
    private final NotificationService service;


    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotify(@PathVariable("id") Integer id) {
        Notification notification = service.getNotification(id);
        return ResponseEntity.ok(notification);
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotify() {
        List<Notification> notifications = service.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/read/{id}")
    public ResponseEntity<Notification> markAsRead(@PathVariable("id") Integer id) {
        var notification = service.markAsRead(id);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/read")
    public ResponseEntity<List<Notification>> getAllRead() {
        List<Notification> notifications = service.getReadNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getAllUnRead() {
        List<Notification> notifications = service.getUnreadNotifications();
        return ResponseEntity.ok(notifications);
    }



}
