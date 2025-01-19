package com.rca.stock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "_notification")
public class Notification {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String category;
    private Double quantity;
    private String message;
    private String recipientEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name="is_read")
    private boolean isRead;
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", quantity=" + quantity +
                ", isRead=" + isRead +
                '}';
    }

}
