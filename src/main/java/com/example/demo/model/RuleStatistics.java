package com.example.demo.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class RuleStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tableName; // 过滤后的新表名称

    @Column(nullable = false)
    private int counts; // 过滤后的数据数目

    @Column(nullable = false)
    private int originCounts; // 原始数据表的数据数目

    @Column(nullable = false)
    private String ruleName; // 应用的规则名称

    @Column(nullable = false)
    private String dataName; // 应用的数据表名称

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
