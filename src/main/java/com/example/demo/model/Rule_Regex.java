// 这是基于正则表达式的规则表
// 字段：

package com.example.demo.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

@Table(name = "rule_regex")
@Data
@Entity
public class Rule_Regex {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ruleName;

    @Column
    private String description;

    @Column(nullable = false, length = 2048)
    @Convert(converter = ListConverter.class)
    private List<String> regex;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
