package com.rca.stock.mapper;

import com.rca.stock.dto.notification.NotificationMessage;
import com.rca.stock.model.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationMapper {

    public Notification toNotification(NotificationMessage dto){
        if(dto == null){
            return null;
        }

        return Notification.builder()
                .name(dto.name())
                .message(dto.message())
                .category(dto.category())
                .quantity(dto.quantity())
                .user(dto.user())
                .recipientEmail(dto.recipientEmail())
                .build();
    }


    public NotificationMessage fromNotification(Notification notification){
        return new NotificationMessage(
                notification.getName(),
                notification.getCategory(),
                notification.getMessage(),
                notification.getQuantity(),
                notification.getUser(),
                notification.getRecipientEmail()

        );
    }
}
