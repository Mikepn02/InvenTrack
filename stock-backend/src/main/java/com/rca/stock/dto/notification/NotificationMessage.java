package com.rca.stock.dto.notification;

import com.rca.stock.model.User;

public record NotificationMessage(
        String name,
        String category,
        String message,
        Double quantity,
        User user,
        String recipientEmail
) {
}
