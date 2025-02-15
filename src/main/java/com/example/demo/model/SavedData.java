//假设存储的数据类型具有下面的格式

package com.example.demo.model;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Data
@Entity
public class SavedData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String saved_data;

    @Column(nullable = false)
    private LocalDate saved_date;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
