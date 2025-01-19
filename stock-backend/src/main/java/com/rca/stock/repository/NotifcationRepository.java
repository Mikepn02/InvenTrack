package com.rca.stock.repository;

import com.rca.stock.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifcationRepository extends JpaRepository<Notification , Integer> {
    List<Notification> findByIsRead(boolean isRead);
}
